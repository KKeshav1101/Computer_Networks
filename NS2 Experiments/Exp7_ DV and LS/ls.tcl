set ns [new Simulator]
$ns rtproto LS
set tf [open out_ls.tr w]
$ns trace-all $tf
set nf [open out_ls.nam w]
$ns namtrace-all $nf

set ft [open "lsr_th" w]

set node0 [$ns node]
set node1 [$ns node]
set node2 [$ns node]
set node3 [$ns node]
set node4 [$ns node]
set node5 [$ns node]
set node6 [$ns node]

$ns duplex-link $node0 $node1 1.5Mb 10ms DropTail
$ns duplex-link $node1 $node2 1.5Mb 10ms DropTail
$ns duplex-link $node2 $node3 1.5Mb 10ms DropTail
$ns duplex-link $node3 $node4 1.5Mb 10ms DropTail
$ns duplex-link $node4 $node5 1.5Mb 10ms DropTail
$ns duplex-link $node5 $node6 1.5Mb 10ms DropTail
$ns duplex-link $node6 $node0 1.5Mb 10ms DropTail

set tcp2 [new Agent/TCP]
$ns attach-agent $node0 $tcp2
set sink2 [new Agent/TCPSink]
$ns attach-agent $node3 $sink2
$ns connect $tcp2 $sink2
set ftp1 [new Application/FTP]
$ftp1 attach-agent $tcp2
$ns at 0.5 "$ftp1 start"

proc record {} {
	global sink2 tf ft
	global ftp1
	set ns [Simulator instance]
	set time 0.1
	set now [$ns now]
	set bw0 [$sink2 set bytes_]
	puts $ft "$now [expr $bw0/$time*8/1000000]"
	$sink2 set bytes_ 0
	$ns at [expr $now+$time] "record"
}

proc finish {} {
	global ns nf
	$ns flush-trace
	close $nf
	exec nam out_ls.nam &
	exec xgraph lsr_th &
	exit 0
}

$ns rtmodel-at 2.0 down $node2 $node3
$ns rtmodel-at 3.0 up $node2 $node3

$ns at 0.55 "record"
$ns at 3.0 $ftp1 stop
$ns at 5.0 "finish"
