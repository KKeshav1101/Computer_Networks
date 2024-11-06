//wired.awk
BEGIN{
  send=0;
  rcv=0;
  dropped=0;
  start=1.0;
  stop=3.0;
}
{
  if($1=="/+/") {
    send++;
  }
  if($5=="tcp") {
    if($1=="r") {
      rcv++;
    }
  }
  if($1=="d") {
    dropped++;
  }
}
END{
  print "Number of packets received" rcv;
  print "throughput=" rcv*8/start-stop "bits per second";
  print "number of packets dropped=" dropped;
}
