# Final Skeleton
#
# Hints/Reminders from Lab 4:
# 
# To send an OpenFlow Message telling a switch to send packets out a
# port, do the following, replacing <PORT> with the port number the 
# switch should send the packets out:
#
#    msg = of.ofp_flow_mod()
#    msg.match = of.ofp_match.from_packet(packet)
#    msg.idle_timeout = 30
#    msg.hard_timeout = 30
#
#    msg.actions.append(of.ofp_action_output(port = <PORT>))
#    msg.data = packet_in
#    self.connection.send(msg)
#
# To drop packets, simply omit the action.
#

from pox.core import core
import pox.openflow.libopenflow_01 as of

log = core.getLogger()

class Final (object):
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

  def do_final (self, packet, packet_in, port_on_switch, switch_id):
	 msg = of.ofp_flow_mod()
	 msg.match = of.ofp_match.from_packet(packet)
	 msg.idle_timeout = 30
	 msg.hard_timeout = 30
	 msg.data = packet_in
	 arp = packet.find('arp')
	 icmp = packet.find('icmp')
	 tcp = packet.find('tcp')
	 ipv4 = packet.find('ipv4')
	 h1 = '10.0.1.10'
	 h2 = '10.0.2.20'
	 h3 = '10.0.3.30'
	 th = '104.82.214.112'
	 uth = '156.134.2.12'
	 ser = '10.0.4.10'
	 if arp is not None:
		 msg.match.dl_type = 0x0806
		 msg.actions.append(of.ofp_action_output(port = of.OFPP_FLOOD))
		 self.connection.send(msg)
	 elif ipv4 is not None:
		 if icmp is not None:
			 if switch_id == 4:
				 if (ipv4.dstip == h1) and (ipv4.srcip != uth):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 elif (ipv4.dstip == h2) and (ipv4.srcip != uth):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
				 elif (ipv4.dstip == h3) and (ipv4.srcip != uth):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 3))
					 self.connection.send(msg)
				 elif (ipv4.dstip == th):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 4))
					 self.connection.send(msg)
				 elif (ipv4.dstip == ser) and (ipv4.srcip != uth):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 6))
					 self.connection.send(msg)
				 elif (ipv4.dstip == uth) and (ipv4.srcip == th):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 5))
					 self.connection.send(msg)
				 else:
					 self.connection.send(msg)
			 if switch_id == 1:
				 if (ipv4.dstip == h1):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
			 if switch_id == 2:
				 if (ipv4.dstip == h2):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
			 if switch_id == 3:
				 if (ipv4.dstip == h3):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
			 if switch_id == 5:
				 if (ipv4.dstip == ser):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
		 else:
			 msg.match.nw_proto = 6
			 if switch_id == 4:
				 if (ipv4.dstip == h1):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 elif (ipv4.dstip == h2):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
				 elif (ipv4.dstip == h3):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 3))
					 self.connection.send(msg)
				 elif (ipv4.dstip == th):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 4))
					 self.connection.send(msg)
				 elif (ipv4.dstip == ser) and (ipv4.srcip != uth):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 6))
					 self.connection.send(msg)
				 elif (ipv4.dstip == uth):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 5))
					 self.connection.send(msg)
				 else:
					 self.connection.send(msg)
			 if switch_id == 1:
				 if (ipv4.dstip == h1):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
			 if switch_id == 2:
				 if (ipv4.dstip == h2):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
			 if switch_id == 3:
				 if (ipv4.dstip == h3):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
					 self.connection.send(msg)
			 if switch_id == 5:
				 if (ipv4.dstip == ser):
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 1))
					 self.connection.send(msg)
				 else:
					 msg.match.nw_src = ipv4.srcip
					 msg.match.nw_dst = ipv4.dstip
					 msg.actions.append(of.ofp_action_output(port = 2))
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
    self.do_final(packet, packet_in, event.port, event.dpid)

def launch ():
  """
  Starts the component
  """
  def start_switch (event):
    log.debug("Controlling %s" % (event.connection,))
    Final(event.connection)
  core.openflow.addListenerByName("ConnectionUp", start_switch)
