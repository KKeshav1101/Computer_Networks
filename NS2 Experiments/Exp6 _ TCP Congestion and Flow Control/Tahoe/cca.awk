BEGIN{
	st1=0
	ft1=0
	throughput1=0
	delay1=0
	flag1=0
	data1=0
	
	st2=0
	ft2=0
	throughput2=0
	delaye2=0
	flag2=0
	data2=0
	
	st3=0
	ft3=0
	throughput3=0
	delay3=0
	flag3=0
	data3=0
	
	total_delay=0
	total_th=0
}
{
	if($1=='r' && $4==7) { #http
		data1+=$6
		if (flag1==0) {
			st1=$2
			flag1=1
		}
		if (flag1==1) {
			ft1=$2
		}
	}
	if($1=='r' && $4==8) { #ftp
		data2+=$6
		if(flag2==0){
			st2=$2
			flag2=1
		}
		if(flag2==1){
			ft2=$2
		}
	}
	
	if($1=='r' && $4==6) { #smtp
		data3+=6
		if(flag3==0){
			st3=$2
			flag3=1
		}
		if(flag3==1){
			ft3=$2
		}
	}
}
END{
	delay1=ft1-st1
	throughput1=data1/delay1
	printf("THROUGHPUT %f\n",throughput1)
	delay2=ft2-st2
	throughput2=data2/delay2
	printf("Throughput %f\n",throughput2)
	delay3=ft3-st3
	throughput3=data3/delay3
	printf("%f\n",throughput3)
	total_th=throughput1+throughput2+throughput3
	total_delay=delay1+delay2+delay3
	printf("Avg throughput %f\n",total_th/3)
	printf("Avg delay%f\n",total_delay/3)
}
