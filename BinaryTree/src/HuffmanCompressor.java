import edu.neumont.io.Bits;


public class HuffmanCompressor
{
	public byte[] compress(HuffmanTree tree, byte[] b)
	{
		Bits bits=new Bits();
		for(int i=0; i<b.length; i++)
		{
			tree.fromByte(b[i], bits);
		}
		
		byte[] setUp=new byte[bits.size()/8+1];

		int selector=0;
		int update=8;
		
		while(bits.size()>0)
		{
			int temp;
			
			if(bits.poll())
			{
				temp=1;
			}
			else
			{
				temp=0;
			}
			
			temp=(temp<<update-1);
			
			setUp[selector]+=temp;
			
			update--;
			if(update==0)
			{
				update=8;
				selector++;
			}
		}
		
		return setUp;
	}
	
	public byte[] decompress(HuffmanTree tree, int uncompressedLength, byte[] b)
	{
		byte[] ret=new byte[uncompressedLength];
		Bits bits=new Bits();
		int selector=0;
		int update=8;
		
		for(int i=0; i<b.length*8; i++)
		{
			if(((b[selector]>>(update-1))&1)!=0)
			{
				bits.add(true);
			}
			else
			{
				bits.add(false);
			}
			
			update--;
			
			if(update==0)
			{
				update=8;
				selector++;
			}
		}
		
		int setUp=0;
		while((uncompressedLength*8)>setUp*8)
		{
			ret[setUp]=tree.toByte(bits);
			setUp++;
		}
		
		return ret;	
	}
}
