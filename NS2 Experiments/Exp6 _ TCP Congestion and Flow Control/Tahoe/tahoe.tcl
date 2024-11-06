set ns [ new Simulator ]

set tf [open tahoe.tr w]
$ns trace-all $tf

set nf [open tahoe.nam w]
$ns namtrace-all $nf

set ft3 [open tahoe_sender_throughput w]

proc finish {} {
	global ns nf ft3
	$ns flush-trace
	close $nf
	close $ft3
	exec xgraph tahoe_sender_throughput &
	puts "running nam..."
	exec nam tahoe.nam &
	exit 0
}

proc record {} {
	global null3 ft3
	global http1
	set ns [Simulator instance]
	set time 0.1
	set now [$ns now]
	set bw2 [$null3 set bytes_]
	puts $ft3 "$now [expr $bw2/$time*8/100000]"
	$null3 set bytes_ 0
	$ns at [expr $now+$time] "record"
}

for {set i 0} {$i < 6} {incr i} {
	set n($i) [$ns node]
}

$ns duplex-link $n(0) $n(1) 10Kb 10ms DropTail
$ns duplex-link $n(0) $n(3) 100Kb 10ms RED
$ns duplex-link $n(1) $n(2) 50Kb 10ms DropTail
$ns duplex-link $n(2) $n(5) 200Kb 10ms RED
$ns duplex-link $n(3) $n(4) 70Kb 10ms DropTail
$ns duplex-link $n(4) $n(5) 100Kb 10ms DropTail

set tcp3 [new Agent/TCP]
set null3 [new Agent/TCPSink]
$ns attach-agent $n(0) $tcp3
$ns attach-agent $n(5) $null3
$ns connect $tcp3 $null2
set http1 [new Application/Traffic/Exponential]
$http1 attach-agent $tcp3

$ns at 0.5 "record"
$ns at 0.2 "$ns trace-annotate \"Starting HTTP from 0 to 5\""
$ns at 0.2 "$http1 start"
$ns at 3.2 "$http1 stop"
$ns at 5.0 "finish"
$ns run

