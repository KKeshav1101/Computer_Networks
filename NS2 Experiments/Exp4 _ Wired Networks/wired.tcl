set ns [new Simulator]
set tf [open wired.tr w]
$ns trace-all $tf
set nf [open wired.nam w]
$ns namtrace-all $nf

set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]

$ns duplex-link $n0 $n1 5Mb 2ms DropTail
$ns duplex-link $n1 $n2 10Mb 5ms DropTail
$ns duplex-link $n1 $n4 3Mb 10ms DropTail
$ns duplex-link $n4 $n3 100Mb 2ms DropTail
$ns duplex-link $n4 $n5 4Mb 10ms DropTail

set udp [new Agent/UDP]
set null [new Agent/Null]
$ns attach-agent $n0 $udp
$ns attach-agent $n3 $null
$ns connect $udp $null
set cbr [new Application/Traffic/CBR]
$cbr attach-agent $udp

set tcp [new Agent/TCP]
set sink [new Agent/TCPSink]
$ns attach-agent $n1 $tcp
$ns attach-agent $n5 $sink
$ns connect $tcp $sink
set ftp [new Application/FTP]
$ftp attach-agent $tcp

$ns at 1.0 "$cbr start"
$ns at 2.0 "$ftp start"
$ns at 10.0 finish

proc finish {} {
	global ns tf nf
	$ns flush-trace
	exec nam wired.nam &
	close $tf
	close $nf
	exit 0
}
$ns run
