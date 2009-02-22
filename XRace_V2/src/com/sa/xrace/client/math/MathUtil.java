///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id: $
 * Last Commit:  $Author: $
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.math;

public final class MathUtil {
	
	public static final int BIG_ENDIAN=0x02;		//����,��λ��ǰ
	public static final int LITTLE_ENDIAN=0x04;	//����,��λ��ǰ
	
	private MathUtil() {
		// Do noting
	}

	/**
	 * ����ָ���洢˳��byte[]ת��Ϊint
	 */
	public static int byte2int(byte[] b,int endian)
	{
		int result=0;
		if(endian==LITTLE_ENDIAN)
		{
			for(int i=0;i<b.length;i++)
			{
				result|=((b[i]&0xff)<<i*8);
			}
		}
		else
		{
			for(int i=b.length-1;i>=0;i--)
			{
				result|=((b[i]&0xff)<<(b.length-i-1)*8);
			}
		}
		return result;
	}
	
	/**
	 * ��int��תΪbyte[]����
	 * @param num
	 * @param endian ˳��
	 * @return
	 */
	public static byte[] int2byte(int num,int endian)
    {
        byte[] targets=new byte[4];
        if(endian==LITTLE_ENDIAN)
        {
        	for(int i=0;i<4;i++)
        	{
        		targets[i]=(byte)((num>>i*8)&0xff);
        	}
        }
        else
        {
        	for(int i=3;i>=0;i--)
        	{
        		targets[i]=(byte)((num>>(3-i)*8)&0xff);
        	}
        }
        return targets;
    }
	
	/**
	 * @description	���ַ�����ʽ���ʮ����������ʮ��������ʽ
	 * @param n
	 * @return
	 */
	public static String int2Hex(int n) {
	       StringBuffer buf = new StringBuffer();
	       int hi = n >> 8 ;  		//��16λ
	       int lo = n&0x00ff;  		//��16λ
	       String hig = Integer.toHexString(hi);
	       String low = Integer.toHexString(lo);
	       buf.append("0x");
	       buf.append(hig);
	       buf.append(low);
	       while(buf.length()<6)buf.append("0");
	       return new String(buf);
	    }
	
//	public static void main(String[] args)
//	{
//		byte[] b= {1,2,2};
//		System.out.println(MathUtil.byte2int(b,BIG_ENDIAN));
//	}

}

