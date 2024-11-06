set val(chan) Channel/WirelessChannel;
set val(prop) Propagation/TwoRayGround;
set val(netif) Phy/WirelessPhy;
set val(mac) Mac/802_11;
set val(ifq) Queue/DropTail/PriQueue;
set val(ll) LL;
set val(ant) Antenna/OmniAntenna;
set val(ifqlen) 50;
set val(nn) 6;
set val(rp) AODV;
set val(x) 500;
set val(y) 500;

set ns [new Simulator]

set tracefile [open wireless.tr w]
$ns trace-all $tracefile

set namfile [open wireless.nam w]
$ns namtrace-all-wireless $namfile $val(x) $val(y)

set f0 [open thruput w]
set topo [new Topography]
$topo load_flatgrid $val(x) $val(y)

create-god $val(nn)
set channel1 [new $val(chan)]

$ns node-config -adhocRouting $val(rp) \
	-llType $val(ll)\
	-macType $val(mac)\
	-ifqType $val(ifq)\
	-ifqLen $val(ifqlen)\
	-antType $val(ant)\
	-propType $val(prop)\
	-phyType $val(netif)\
	-topoInstance $topo \
	-agentTRace ON\
	-maxTrace ON\
	-routerTrace ON\
	-movementTrace ON\
	-channel $channel1    

proc record {} {
	global sink1 sink2 f0
	set ns [Simulator instance]
	set time 0.5
	set bw1 [$sink1 set bytes_]
	set bw2 [$sink2 set bytes_]
	set now [$ns now]
	puts $f0 "$now [expr($bw1+$bw2)/$time*8/1000000]"
	$sink1 set bytes_ 0
	$sink2 set bytes_ 0
	$ns at [expr $now + $time] "record"
}

set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]

$n0 random-motion 0
$n1 random-motion 0
$n2 random-motion 0
$n3 random-motion 0
$n4 random-motion 0
$n5 random-motion 0

$ns initial_node_pos $n0 20
$ns initial_node_pos $n1 20
$ns initial_node_pos $n2 20
$ns initial_node_pos $n3 20
$ns initial_node_pos $n4 20
$ns initial_node_pos $n5 50

#initial coordinates of the nodes 
$n0 set X_ 10.0
$n0 set Y_ 20.0
$n0 set Z_ 0.0

$n1 set X_ 210.0
$n1 set Y_ 230.0
$n1 set Z_ 0.0

$n2 set X_ 100.0
$n2 set Y_ 200.0
$n2 set Z_ 0.0

$n3 set X_ 150.0
$n3 set Y_ 230.0
$n3 set Z_ 0.0

$n4 set X_ 430.0
$n4 set Y_ 320.0
$n4 set Z_ 0.0

$n5 set X_ 270.0
$n5 set Y_ 120.0
$n5 set Z_ 0.0

$ns at 1.0 "$n1 setdest 490.0 340.0 25.0"
$ns at 1.0 "$n4 setdest 300.0 130.0 5.0"
$ns at 1.0 "$n5 setdest 190.0 440.0 15.0"

$ns at 20.0 "$n5 setdest 100.0 200.0 30.0"

set tcp [new Agent/TCP]
set sink1 [new Agent/LossMonitor]
$ns attach-agent $n0 $tcp
$ns attach-agent $n5 $sink1
$ns connect $tcp $sink1
set ftp [new Application/FTP]
$ftp attach-agent $tcp
$ns at 0.0 "record"
$ns at 1.0 "$ftp start"

set tcp1 [new Agent/TCP]
set sink2 [new Agent/LossMonitor]
$ns attach-agent $n2 $tcp1
$ns attach-agent $n3 $sink2
$ns connect $tcp1 $sink2
set cbr [new Application/FTP]
$cbr attach-agent $tcp1
$ns at 1.0 "$ftp start"

$ns at 30.0 "finish"

proc finish {} {
	global ns tracefile namfile
	$ns flush-trace
	close $tracefile
	close $namfile
	exit 0
}

puts "Starting Simulation"
$ns run

