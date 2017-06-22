import httplib
import sys
import time

#conn = httplib.HTTPConnection(sys.argv[1], 8080, source_address=("10.0.0.4",12345))
while True :
    conn = httplib.HTTPConnection(sys.argv[1], 80)
    conn.request("GET", "/", headers={"Connection":" keep-alive"})
    response = conn.getresponse()
    data = response.read()
    print data
    time.sleep(1)
    conn.close()
