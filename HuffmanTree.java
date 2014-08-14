import java.util.List;
import java.util.PriorityQueue;

import edu.neumont.io.Bits;

public class HuffmanTree
{
	PriorityQueue<Node> pq=new PriorityQueue<Node>();
	
	private static class Node implements Comparable<Node>
	{
		Node less, greater;
		Byte data;
		float frequency;
		boolean hub;
		
		public Node(Byte data, float freq, boolean hubCreator)
		{
			this.data=data;
			this.frequency=freq;
			this.hub=hubCreator;
		}
		
		public boolean isHub()
		{
			return hub;
		}

		@Override
		public int compareTo(Node o) {
			return (int) (1000000*this.frequency - 1000000*o.frequency);
		}
	}
	
	public HuffmanTree(byte[] bytes)
	{
		if(bytes==null || bytes.length==0)
			return ;
		
		int[] frequency=new int[256];
		int numUniqueFreq=0;
		
		for(byte b: bytes)
		{
			frequency[b]++;
			numUniqueFreq++;
		}
		
		for(int i=0; i<frequency.length; i++)
		{
			if(frequency[i]>0)
			{
				Node temp=new Node((byte)i, (float)frequency[i]/numUniqueFreq, false);
				pq.add(temp);
			}
		}
		
		while(pq.size()>1)
		{
			Node les=pq.poll();
			Node great=pq.poll();
			Node temp=new Node((Byte)null, great.frequency+les.frequency, true);
			temp.greater=great;
			temp.less=les;
			pq.add(temp);
		}
	}
	
	public void recalculateTree(int[] frequency, int numUniqueFreq)
	{
		for(int i=0; i<frequency.length; i++)
		{
			if(frequency[i]>0)
			{
				Node temp=new Node((byte)(i-128), (float)frequency[i]/numUniqueFreq, false);
				pq.add(temp);
			}
		}
		
		while(pq.size()>1)
		{
			Node les=pq.poll();
			Node great=pq.poll();
			Node temp=new Node((Byte)null, great.frequency+les.frequency, true);
			temp.greater=great;
			temp.less=les;
			pq.add(temp);
		}
	}
	
	public byte toByte(Bits bits)
	{
		return toByteHelper(bits, pq.peek());
	}
	private byte toByteHelper(Bits bits, Node root)
	{
		byte ret;

		if(bits.peek()==null)
			return root.data;

		if(bits.peek())
		{
			if(root.greater!=null)
			{
				bits.poll();
				ret=toByteHelper(bits, root.greater);
			}
			else
			{
				return root.data;
			}
		}
		else
		{
			if(root.less!=null)
			{
				bits.poll();
				ret=toByteHelper(bits, root.less);
			}
			else
			{
				return root.data;
			}
		}
		return ret;
	}
	
	public void fromByte(byte b, Bits bits)
	{
		fromByteHelper(b, bits, pq.peek());
	}
	private boolean fromByteHelper(byte b, Bits bits, Node root)
	{
		boolean ret=false;
		if(root.greater!=null && !ret)
		{
			bits.add(true);
			ret=fromByteHelper(b, bits, root.greater);
			if(!ret)
				bits.remove(bits.size()-1);
		}
		
		if(root.data!=null && root.data==b)
		{
			return true;
		}
		
		if(root.less!=null && !ret)
		{
			bits.add(false);
			ret=fromByteHelper(b, bits, root.less);
			if(!ret)
				bits.remove(bits.size()-1);
		}

		return ret;
	}
	
	public void preOrder()
	{
		preOrderHelp(pq.peek());
	}
	private void preOrderHelp(Node root)
	{
		if(root.isHub())
		{
			System.out.println("Hub...Frequency: " + root.frequency);
		}
		else
		{
			System.out.println("NotHub...Frequency: " + root.frequency + " Value: " + root.data);
		}
		if(root.greater!=null)
			preOrderHelp(root.greater);
		if(root.less!=null)
			preOrderHelp(root.less);
	}
	
	public void inOrder()
	{
		inOrderHelp(pq.peek());
	}
	private void inOrderHelp(Node root)
	{
		if(root.greater!=null)
			inOrderHelp(root.greater);
		
		if(root.isHub())
		{
			System.out.println("Hub...Frequency: " + root.frequency);
		}
		else
		{
			System.out.println("NotHub...Frequency: " + root.frequency + " Value: " + root.data);
		}
		
		if(root.less!=null)
			inOrderHelp(root.less);
	}
	
	public void postOrder()
	{
		postOrderHelp(pq.peek());
	}
	private void postOrderHelp(Node root)
	{
		if(root.greater!=null)
			postOrderHelp(root.greater);
		
		if(root.less!=null)
			postOrderHelp(root.less);
		
		if(root.isHub())
		{
			System.out.println("Hub...Frequency: " + root.frequency);
		}
		else
		{
			System.out.println("NotHub...Frequency: " + root.frequency + " Value: " + root.data);
		}
	}
}
