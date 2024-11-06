BEGIN{
	drop=0
	recv=0
	starttime1=0
	endtime1=0
	latency1=0
	filesize1=0
}
{
	if($1=="r" && $3==6){
		if(flag1==0){
			flag1=1
			startime1=$2
		}
		filsize1+=$6
		endtime1=$2
		latency=endtime1-starttime1
		bandwidth1=filesize1/latency
		printf "%f %f\n",endtime1,bandwidth1 >> "file3.xg"
	}
}
END{
	print("\n\n\n FINAL VALUES..")
	print("\n\nfilesize:",filesize1)
	latency=entime1-starttime1
	print("\nlatency:",latency)
	bandwidth1=filesize1/latency
	printf("\nThroughput (MBps):"bandwidth1/10^6)
}
