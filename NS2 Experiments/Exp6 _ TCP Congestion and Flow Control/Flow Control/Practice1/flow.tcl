set ns [new Simulator]
set tf [open trace1.tr w]
$ns trace-all$tf
set nf [open opnam.nam w]
$ns namtrace-all $nf

set ft1 [open "sender1_throughput" 'w']
set ft2 [open "sender2_throughput" "w"]
set ft3 [open "sender3_throughput" "w"]
set ft4 [open "sender4_throughput" "w"]

set fb1 [open "bandwidth1" "w"]
set fb2 [open "bandwidth2" "w"]
set fb3 [open "bandwidth3" "w"]
set fb4 [open "totalbandwidth" "w"]

proc finish {} {
	global ns nf ft1 ft2 ft3 fb1 fb2 fb3 fb3 ft4
	$ns flush-trace
	close $nf
	close $ft1 $ft2 $ft3 $ft4 $fb1 $fb2 $fb3 $fb4
	exec xgraph sender1_throughput sender2_throughput sender3_throughput total_throughput &
	exec xgraph bandwidth1 bandwidth2 bandwidth3 totalbandwidth
	puts "running nam"
	exec nam opnam.nam &
	exit 0	
}

proc record {} {
	global null1 null2 null3 ft1 ft2 ft3 ft4 fb1 fb2 fb3 fb4
	global ftp1 smtp1 http1
	
	set ns [Simulator instance]
	set time 0.1
	set now [$ns now]
	
	set bw0 [$null1 set bytes_]
	set bw1 [$null2 set bytes_]
	set bw3 [$null3 set bytes_]
	set totbw [expr $bw0 + $bw1 + $bw2]
	
	puts $ft4 "$now [expr $totbw/$time*8/1000000]"
	puts $ft1 "$now [expr $bw0/$time*8/1000000]"
	puts $ft2 "$now [expr $bw2/$time*8/1000000]"
	puts $fb1 "$now [expr $bw0]"
	puts $fb2 "$now [expr $bw1]"
	puts $fb3 "$now [expr $bw2]"
	puts $fb4 "$now [expr $totbw]"
	
	$null1 set bytes_ 0
	$null2 set bytes_ 0
	$null3 set bytes_ 0
	
	$ns at [expr $now+$time] "record"
}

for {set i 0} {$i < 10} {incr i} {
	set n($i)  [$ns node]
}

$ns duplex-link $n(0) $n(1) 1Mb 10ms DropTail
$ns duplex-link $n(0) $n(3) 1.5Mb 10ms RED
$ns duplex-link $n(1) $n(2) 1Mb 10ms DropTail
$ns duplex-link $n(2) $n(7) 2Mb 10ms RED
$ns duplex-link $n(7) $n(8) 2Mb 10ms DropTail
$ns duplex-link $n(8) $n(9) 2Mb 10ms RED
$ns duplex-link $n(3) $n(5) 1Mb 10ms DropTail
$ns duplex-link $n(5) $n(6) 1Mb 10ms RED
$ns duplex-link $n(6) $n(4) 1Mb 10ms DropTail
$ns duplex-link $n(4) $n(7) 1Mb 10ms RED

proc ftp_traffic {node0 node9 } {
	global ns null1 tcp1 ftp1
	set tcp1 [new Agent/TCP]
	set null1 [new Agent/TCPSink]
	$ns attach-agent $node0 $tcp1
	$ns attach-agent $node9 $null1
	$ns connect $tcp1 $null1
	set ftp1 [new Application/FTP]
	$ftp1 attach-agent $tcp1
	$ns at 1.0 $ftp1 start
	4ns at 3.2 $ftp stop
}

ftp_traffic $n(0) $n(8)

proc smtp_traffic {node0 node3 } {
	global ns null2 tcp2 smtp1
	set tcp2 [new Agent/TCP]
	set null2 [new Agent/TCPSink]
	$ns attach-agent $node0 $tcp2
	$ns connect $tcp2 $null2
	set smpt1 [new Application/Traffic/Exponential]
	$smpt1 attach-agent $tcp2
	$ns at 2.0 "$smtp1 start"
	$ns at 3.0 "$smtp1 stop"
}
smtp_traffic $n(3) $n(6)

proc http_traffic {node1 node7 } {
	global ns null3 tcp3 http1
	set tcp3 [new Agent/TCP]
	set null3 [new Agent/TCPSink]
	$ns attach-agent $node1 $tcp3
	$ns attach-agent $node7 $null3
	$ns connect $tcp3 $null3
	set http1 [new Application/Traffic/Exponential]
	$http1 attach-agent $tcp3
	$ns at 0.2 "$http1 start"
	$ns at 3.2 "$http1 stop"
}
http_traffic $n(0) $n(7)

$ns at 0.5 "record"
$ns at 5.0 "finish"
$ns run
