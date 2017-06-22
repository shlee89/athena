#!/usr/bin/python

"""
Custom topology for Mininet, generated by GraphML-Topo-to-Mininet-Network-Generator.
"""
from mininet.topo import Topo
from mininet.net import Mininet
from mininet.node import RemoteController
from mininet.node import Node
from mininet.node import CPULimitedHost
from mininet.node import OVSSwitch
from mininet.link import TCLink
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.util import dumpNodeConnections
from functools import partial

class GeneratedTopo( Topo ):
    "Internet Topology Zoo Specimen."

    def __init__( self, **opts ):
        "Create a topology."

        # Initialize Topology
        Topo.__init__( self, **opts )

        # add nodes, switches first...
        s1 = self.addSwitch('s1', dpid='0000000000000001')
        s2 = self.addSwitch('s2', dpid='0000000000000002') 
        s3 = self.addSwitch('s3', dpid='0000000000000003')
        s4 = self.addSwitch('s4', dpid='0000000000000004')
        s5 = self.addSwitch('s5', dpid='0000000000000005')
        s6 = self.addSwitch('s6', dpid='0000000000000006')
        s7 = self.addSwitch('s7', dpid='0000000000000007')
        s8 = self.addSwitch('s8', dpid='0000000000000008') 

        # ... and now hosts
        s1_host = self.addHost('h1', mac='00:00:00:00:00:01')
        s5_host = self.addHost('h5', mac='00:00:00:00:00:05')
        s6_host = self.addHost('h6', mac='00:00:00:00:00:06')
        s8_host = self.addHost('h8', mac='00:00:00:00:00:08')

        # add edges between switch and corresponding host
        self.addLink(s1, s1_host);
        self.addLink(s5, s5_host);
        self.addLink(s6, s6_host);
        self.addLink(s8, s8_host);

        # add edges between switches
        self.addLink(s1, s2, bw=1000)
        self.addLink(s2, s3, bw=1000)
        self.addLink(s3, s4, bw=1000)
        self.addLink(s4, s8, bw=1000)
        self.addLink(s7, s8, bw=1000)
        self.addLink(s4, s7, bw=1000)
        self.addLink(s7, s6, bw=1000)
        self.addLink(s6, s5, bw=1000)
        self.addLink(s2, s6, bw=1000)

topos = { 'generated': ( lambda: GeneratedTopo() ) }

# HERE THE CODE DEFINITION OF THE TOPOLOGY ENDS

# the following code produces an executable script working with a remote controller
# and providing ssh access to the the mininet hosts from within the ubuntu vm
#controller_ip = '192.168.0.11'
#c1 = RemoteController('c1', ip='192.168.0.11')
c1 = RemoteController('c1', ip='127.0.0.1')
def setupNetwork(controller_ip):
    "Create network and run simple performance test"
    # check if remote controller's ip was set
    # else set it to localhost
    topo = GeneratedTopo()
#    if controller_ip == '':
        #controller_ip = '10.0.2.2';
#        controller_ip = '127.0.0.1';
    OVSSwitch13 = partial( OVSSwitch, protocols='OpenFlow13')
    net = Mininet(topo=topo, host=CPULimitedHost, link=TCLink, switch=OVSSwitch13, build=False)
    net.addController(c1)
    net.build();
    net.start()
    CLI(net)
    net.stop();
#    for c in [c1]:
#        net.addController(c)
    return net

def connectToRootNS( network, switch, ip, prefixLen, routes ):
    "Connect hosts to root namespace via switch. Starts network."
    "network: Mininet() network object"
    "switch: switch to connect to root namespace"
    "ip: IP address for root namespace node"
    "prefixLen: IP address prefix length (e.g. 8, 16, 24)"
    "routes: host networks to route to"
    # Create a node in root namespace and link to switch 0
    root = Node( 'root', inNamespace=False )
    intf = TCLink( root, switch ).intf1
    root.setIP( ip, prefixLen, intf )
    # Start network that now includes link to root namespace
    network.start()
    # Add routes from root ns to hosts
    for route in routes:
        root.cmd( 'route add -net ' + route + ' dev ' + str( intf ) )

def sshd( network, cmd='/usr/sbin/sshd', opts='-D' ):
    "Start a network, connect it to root ns, and run sshd on all hosts."
    switch = network.switches[ 0 ]  # switch to use
    ip = '10.123.123.1'  # our IP address on host network
    routes = [ '10.0.0.0/8' ]  # host networks to route to
    connectToRootNS( network, switch, ip, 8, routes )
    for host in network.hosts:
        host.cmd( cmd + ' ' + opts + '&' )

    # DEBUGGING INFO
    print
    print "Dumping host connections"
    dumpNodeConnections(network.hosts)
    print
    print "*** Hosts are running sshd at the following addresses:"
    print
    for host in network.hosts:
        print host.name, host.IP()
    print
    print "*** Type 'exit' or control-D to shut down network"
    print
    print "*** For testing network connectivity among the hosts, wait a bit for the controller to create all the routes, then do 'pingall' on the mininet console."
    print

    CLI( network )
    for host in network.hosts:
        host.cmd( 'kill %' + cmd )
    network.stop()


if __name__ == '__main__':
    setLogLevel('info')
    #setLogLevel('debug')
    setupNetwork(c1)