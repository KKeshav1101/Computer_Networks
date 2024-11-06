BEGIN{
	seqno = -1;
	droppedPackets = 0;
	receivedPackets = 0;
	count = 0;
	n_to_n_delay = 0;
}
{
	#packet delivery ratio
	if($1=='s' && seqno < $6) {
		seqno = $6;
	} else if (($1=='r')) {
		receivedPackets++;
	} else if (($1=='D' && $7=='tcp' && $8 > 512)) {
		droppedPackets++;
	}
	
	#end-to-end delay
	if( $1 == "s" ) {
		start_time[$6] = $2;
	} else if (($7 == "tcp") && ($1 == 'r')) {
		end_time[$6] = $2;
	} else if (($1=="D") && ($7=='tcp')) {
		end_time[$6] = -1;
	}
}
END{
	for(i=0;i<=seqno;i++) {
		if(end_time[i]>0) {
			delay[i] = end_time[i] - start_time[i];
			n_to_n_delay += delay[i];
			count++;
		}
	}
	
	if (count>0){
		avg_delay = n_to_n_delay / count;
	} else {
		avg_delay = 0;
	}
	
	print "\n";
	print "Generated Packets =" seqno + 1;
	print "Received Packets =" receivedPackets;
	print "Packet Delivery Ratio =" (seqno + 1 > 0 ? (receivedPackets / (seqno + 1)*100):0) "%";
	print "Total Dropped Packets =" droppedPackets;
	print "Average end to end delay =" (count > 0 ? (avg_delay*1000):0) "ms";
	print "\n";
}
