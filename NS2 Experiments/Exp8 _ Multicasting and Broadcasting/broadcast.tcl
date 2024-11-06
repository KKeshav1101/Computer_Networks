set ns [new Simulator -multicast on]

set tf [open output.tr w]
$ns trace-all $tf

set fd [open mcast.nam w]
$ns namtrace-all $fd

set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]
set n6 [$ns node]
set n7 [$ns node]

$ns duplex-link $n0 $n2 1.5Mb 10ms DropTail
$ns duplex-link $n1 $n2 1.5Mb 10ms DropTail
$ns duplex-link $n2 $n3 1.5Mb 10ms DropTail
$ns duplex-link $n3 $n4 1.5Mb 10ms DropTail
$ns duplex-link $n3 $n7 1.5Mb 10ms DropTail
$ns duplex-link $n4 $n5 1.5Mb 10ms DropTail
$ns duplex-link $n4 $n6 1.5Mb 10ms DropTail

set mproto DM
set mrthandle [$ns mrtproto $mproto {}]

set group1 [Node allocaddr]

set udp0 [new Agent/UDP]
$ns attach-agent $n0 $udp0
$udp0 set dst_addr_ $group1
$udp0 set dst_port_ 0
set cbr1 [new Application/Traffic/CBR]
$cbr1 attach-agent $udp0

set rcvr1 [new Agent/Null]
$ns attach-agent $n1 $rcvr1
$ns at 2.0 "$n1 join-group $rcvr1 $group1"

set rcvr2 [new Agent/Null]
$ns attach-agent $n2 $rcvr2
$ns at 2.0 "$n2 join-group $rcvr2 $group1"

set rcvr3 [new Agent/Null]
$ns attach-agent $n3 $rcvr3
$ns at 2.0 "$n3 join-group $rcvr3 $group1"

set rcvr4 [new Agent/Null]
$ns attach-agent $n4 $rcvr4
$ns at 2.0 "$n4 join-group $rcvr4 $group1"

set rcvr5 [new Agent/Null]
$ns attach-agent $n5 $rcvr5
$ns at 2.0 "$n5 join-group $rcvr5 $group1"

set rcvr6 [new Agent/Null]
$ns attach-agent $n6 $rcvr6
$ns at 2.0 "$n6 join-group $rcvr6 $group1"

set rcvr7 [new Agent/Null]
$ns attach-agent $n7 $rcvr7
$ns at 2.0 "$n7 join-group $rcvr7 $group1"

$ns at 4.0 "$n1 leave-group $rcvr1 $group1"
$ns at 4.0 "$n2 leave-group $rcvr2 $group1"
$ns at 4.0 "$n3 leave-group $rcvr3 $group1"
$ns at 4.0 "$n4 leave-group $rcvr4 $group1"
$ns at 4.0 "$n5 leave-group $rcvr5 $group1"
$ns at 4.5 "$n6 leave-group $rcvr6 $group1"
$ns at 5.0 "$n7 leave-group $rcvr7 $group1"

$ns at 0.5 "$cbr1 start"
$ns at 9.5 "$cbr1 stop"

$ns at 10.0 "finish"

proc finish {} {
	global ns tf
	$ns flush-trace
	close $tf
	exec nam mcast.nam &
	exit0
}

$ns set-animation-rate 3.0ms
$ns run

