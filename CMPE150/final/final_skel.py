#!/usr/bin/python

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel
from mininet.cli import CLI
from mininet.node import RemoteController

class final_topo(Topo):
  def build(self):
    
    # Examples!
    # Create a host with a default route of the ethernet interface. You'll need to set the
    # default gateway like this for every host you make on this assignment to make sure all 
    # packets are sent out that port. Make sure to change the h# in the defaultRoute area
    # and the MAC address when you add more hosts!
    h1 = self.addHost('h1',mac='00:00:00:00:00:01',ip='10.0.1.10/24', defaultRoute="h1-eth0")
    h2 = self.addHost('h2',mac='00:00:00:00:00:02',ip='10.0.2.20/24', defaultRoute="h2-eth0")
    h3 = self.addHost('h3',mac='00:00:00:00:00:03',ip='10.0.3.30/24', defaultRoute="h3-eth0")
    th = self.addHost('th',mac='00:00:00:00:00:04',ip='104.82.214.112/24', defaultRoute="th-eth0")
    uth = self.addHost('uth',mac='00:00:00:00:00:05',ip='156.134.2.12/24', defaultRoute="uth-eth0")
    ser = self.addHost('ser',mac='00:00:00:00:00:06',ip='10.0.4.10/24', defaultRoute="ser-eth0")

    # Create a switch. No changes here from Lab 1.
    s1 = self.addSwitch('s1')
    s2 = self.addSwitch('s2')
    s3 = self.addSwitch('s3')
    s4 = self.addSwitch('s4')
    s5 = self.addSwitch('s5')

    # Connect Port 8 on the Switch to Port 0 on Host 1 and Port 9 on the Switch to Port 0 on 
    # Host 2. This is representing the physical port on the switch or host that you are 
    # connecting to.
    self.addLink(s1,h1, port1=1, port2=0)
    self.addLink(s1,s4, port1=2, port2=1)
    self.addLink(s2,h2, port1=1, port2=0)
    self.addLink(s2,s4, port1=2, port2=2)
    self.addLink(s3,h3, port1=1, port2=0)
    self.addLink(s3,s4, port1=2, port2=3)
    self.addLink(s4,th, port1=4, port2=0)
    self.addLink(s4,uth, port1=5, port2=0)
    self.addLink(s4,s5, port1=6, port2=2)
    self.addLink(s5,ser, port1=1, port2=0)

def configure():
  topo = final_topo()
  net = Mininet(topo=topo, controller=RemoteController)
  net.start()

  CLI(net)
  
  net.stop()


if __name__ == '__main__':
  configure()
