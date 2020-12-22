# Lab 3 Skeleton
#
# Based on of_tutorial by James McCauley

from pox.core import core
import pox.openflow.libopenflow_01 as of

log = core.getLogger()

class Firewall (object):
  """
  A Firewall object is created for each switch that connects.
  A Connection object for that switch is passed to the __init__ function.
  """
  def __init__ (self, connection):
    # Keep track of the connection to the switch so that we can
    # send it messages!
    self.connection = connection

    # This binds our PacketIn event listener
    connection.addListeners(self)

  def do_firewall (self, packet, packet_in):
    msg = of.ofp_flow_mod()
    msg.match = of.ofp_match.from_packet(packet)
    msg.idle_timeout = 25
    msg.hard_timeout = 50
    msg.data = packet_in
    icmp = packet.find('icmp')
    arp = packet.find('arp')
    tcp = packet.find('tcp')
    ipv4 = packet.find('ipv4')
    if icmp or arp or tcp is not None:
      if icmp is not None:
        msg.match.nw_proto = 1
        msg.actions.append(of.ofp_action_output(port = of.OFPP_FLOOD))
        self.connection.send(msg)
      elif arp is not None:
        msg.match.dl_type = 0x0806
        msg.actions.append(of.ofp_action_output(port = of.OFPP_FLOOD))
        self.connection.send(msg)
      elif tcp is not None:
        msg.match.nw_proto = 6
        if (ipv4.srcip == '10.0.1.10') and (ipv4.dstip == '10.0.1.30'):
          msg.match.nw_src = ipv4.srcip
          msg.match.nw_dst = ipv4.dstip
          msg.actions.append(of.ofp_action_output(port = of.OFPP_FLOOD))
          self.connection.send(msg)
        elif (ipv4.srcip == '10.0.1.30') and (ipv4.dstip == '10.0.1.10'):
          msg.match.nw_src = ipv4.srcip
          msg.match.nw_dst = ipv4.dstip
          msg.actions.append(of.ofp_action_output(port = of.OFPP_FLOOD))
          self.connection.send(msg)
    else:
      self.connection.send(msg)

  def _handle_PacketIn (self, event):
    """
    Handles packet in messages from the switch.
    """

    packet = event.parsed # This is the parsed packet data.
    if not packet.parsed:
      log.warning("Ignoring incomplete packet")
      return

    packet_in = event.ofp # The actual ofp_packet_in message.
    self.do_firewall(packet, packet_in)

def launch ():
  """
  Starts the component
  """
  def start_switch (event):
    log.debug("Controlling %s" % (event.connection,))
    Firewall(event.connection)
  core.openflow.addListenerByName("ConnectionUp", start_switch)
