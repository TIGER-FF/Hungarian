package Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Hungarian1 {

	private Integer[][] originalValues;//�洢ԭʼ�ĳɱ�����
	private Integer[][] processValues;//���ڱ仯�ĳɱ�����
	List<Integer> rowIndex=new ArrayList<Integer>();//���û��-1�����±�
	List<Integer> colIndex=new ArrayList<Integer>();//���û��-1��������-2���±�
	private Map<Integer, Integer> indexMap=new HashMap<Integer, Integer>();//���ڴ洢��������
	//���캯�����ڸտ�ʼ��ԭʼ���ݴ洢��originalValues�����洢��processValues�����ڱ仯
	//���process�洢��Ҫ�õ�clone����
	public  Hungarian1(Integer[][] values) {
		//��Ϊ�����Ϊ�׾��������к���һ��
		int len=values.length;
		originalValues=values;//ԭʼ���ݣ���orginalValues����ָ��ԭ��������
		processValues=MyClone(values);//�õ�һ���µ�������ԭʼ����û�е�ַ����
		boolean flag=false;//��ʾ���indexMap�д洢�����У��������У�true:���У�
		//1���������ÿһ�м�ȥÿ�е���Сֵ
		for(int i=0;i<len;i++)
		{
			//�õ�ÿ�е���Сֵ
			int min=minNum(processValues[i]);
			for(int j=0;j<len;j++)
			{
				//��ÿһ�е�����ȥ��Сֵ
				processValues[i][j]-=min;
				
			}
		
		}
		//2���������ÿһ�м�ȥÿһ�е���Сֵ
		for(int i=0;i<len;i++)
		{
			//�½�һ����ʱһά���������洢ÿһ��
			Integer[] lie=new Integer[len];
			for(int j=0;j<len;j++)
			{
				//ȡ��ÿһ�е�����
				lie[j]=processValues[j][i];
			}
			//�õ�ÿһ�е���Сֵ
			int min=minNum(lie);
			for(int j=0;j<len;j++)
			{
				//��ÿһ�е�ֵ��ȥ�Լ��е���Сֵ
				processValues[j][i]-=min;
			}
		}
		//�����е�֮һ��ʱ���еĳɱ������Ѿ��ﵽ��������Ҫ��Ŀ���ˣ���Ҫ�����ж��Ƿ�ﵽ
		while(!isMatchByRow(processValues))//û�дﵽĿ��
		{
			//3�����߶�����û��-1�Ľ��л��ߣ��Ի���������-2���л��ߣ��Ի���������-1�Ļ���
			//����Ҫ�����ˣ�ֻ��Ҫ�ҳ�Ҫ��Сֵ�ļ��ϣ������Сֵ����
			int min=PaintLine(processValues);
			
			
			//��ԭ����
			for(int i=0;i<processValues.length;i++)
			{
				for(int j=0;j<processValues[i].length;j++)
				{
					if(processValues[i][j]==-1||processValues[i][j]==-2)
						processValues[i][j]=0;
				}
			}//end for
			//��Ҫ�жϻ������Ƿ����lenght����Ҳ֤������
			if(processValues.length==(processValues.length-rowIndex.size()+colIndex.size()))
			{
				//���н����жϽ�����ת��
				if(isMatchByRow(processValues))
				{
					flag=true;
					break;
				}
					
			}
			//�Դ�Ժŵ��м�ȥmin
			for(int i:rowIndex)
			{
				for(int j=0;j<processValues[i].length;j++)
				{
						processValues[i][j]-=min;				
				}
			}//end  for(int i:rowIndex)
			//���м���min
			for(int i:colIndex)
			{
				for(int j=0;j<processValues.length;j++)
				{
						processValues[j][i]+=min;				
				}
			}//end  for(int i:rowIndex)
			//���rowIndex��colIndex
			rowIndex.clear();
			colIndex.clear();
		}//end while
		int count=0;
		if(flag)//flagΪtrueΪkey--��      value----��
		{
			for(Entry<Integer, Integer> en:indexMap.entrySet())
			{
				System.out.println(en.getValue()+1+"------"+(en.getKey()+1));
				count+=originalValues[en.getValue()][en.getKey()];
			}
			
		}else {
			for(Entry<Integer, Integer> en:indexMap.entrySet())
			{
				System.out.println(en.getKey()+1+"--------"+(en.getValue()+1));
				count+=originalValues[en.getKey()][en.getValue()];
			}
		}
		
		System.out.println();
		System.out.println();
		for(int i=0;i<len;i++)
		{
			
			for(int j=0;j<len;j++)
			{
				
				System.out.print(originalValues[i][j]+"  ");
			}
	
			System.out.println();
		}
		System.out.println("count--------"+count);
	}
	//����
	
	
	public int findLessZeroByCol(Integer[][] processValues2) {
		//��ʾÿһ��0�ĸ���
		//��ʼ��Ϊ���
		int n=processValues2.length;
		int index=-1;//��ʾ��һ��0���ٵ��б�
		for(int i=0;i<processValues2.length;i++)
		{
			int n1=0;
			boolean zeroFlag=false;
			for(int j=0;j<processValues2[i].length;j++)
			{
				if(processValues2[i][j]==0)
				{
					n1++;
					zeroFlag=true;
				}
			}
			
			if(zeroFlag&&n>n1)
			{
				//���ڱ�����һ��0�ĸ���
				n=n1;
				//��������0���е��±�
				index=i;
			}
		}

		return index;
	}

	public int PaintLine(Integer[][] processValues2) {
		//-3��ʾ����
		
		for(int i=0;i<processValues2.length;i++)
		{
			if(!Arrays.asList(processValues2[i]).contains(-1))
			{	
				rowIndex.add(i);
				for(int j=0;j<processValues2[i].length;j++)
				{
					if(processValues2[i][j]==-2)
						colIndex.add(j);
				}
			}
		}
		int n;
		do {
			n=addCol(processValues2)+addRow(processValues2);
		} while (n!=0);
		//��û�л��ߵ��������ҳ���Сֵ
		boolean flag=true;
		int min=0;
		for(int i:rowIndex)
		{
			for(int j=0;j<processValues2[i].length;j++)
			{
				if(!colIndex.contains(j))
				{
					if(flag)
					{
						min=processValues2[i][j];
						flag=false;
					}
					if(min>processValues2[i][j])
						min=processValues2[i][j];
				}
				
			}
		}
		return min;
		
	}
	public int addRow(Integer[][] processValues2) {
		int n=0;
		for(Integer i:colIndex)
		{
			for(int j=0;j<processValues2[i].length;j++)
			{
				if(!rowIndex.contains(j)&&processValues2[j][i]==-1)
				{
					n++;
					rowIndex.add(j);
				}
			}
		}
		return n;
	}
	public int addCol(Integer[][] processValues2) {
		int n=0;
		for(Integer i:rowIndex)
		{
			for(int j=0;j<processValues2[i].length;j++)
			{
				if(!colIndex.contains(j)&&processValues2[i][j]==-2)
				{
					n++;
					colIndex.add(j);
				}
			}
		}
		return n;
	}
	//����һ���µ�����ı�������鵫��ԭʼ���ݲ�����ű仯���仯
	public Integer[][] MyClone(Integer[][] values) {
		Integer[][] num_clone=new Integer[values.length][values[0].length];//�½�һ������
		for(int i=0;i<values.length;i++)
		{
			//�����˹�clone����
			num_clone[i]=values[i].clone();
			/*
				//����һ������copy�ķ�������for()ѭ��������ÿһ����ֵ
				System.arraycopy(num[i], 0, num_clone[i], 0, num[i].length);
			*/
		}
		return num_clone;
	}
	//����һ��һά�������С��
	public Integer minNum(Integer[] processValues2) {
		int min=processValues2[0];
		for(int i=1;i<processValues2.length;i++)
		{
			if(min>processValues2[i])
			{
				min=processValues2[i];
			}
		}
		return min;
	}
	//Ѱ��������һ���������С����������
	public int findLessZero(Integer[][] processValues2) {
		//��ʾÿһ��0�ĸ���
		//��ʼ��Ϊ���
		int n=processValues2.length;
		int index=-1;//��ʾ��һ��0���ٵ��б�
		for(int i=0;i<processValues2.length;i++)
		{
			int n1=0;
			boolean zeroFlag=false;
			for(int j=0;j<processValues2[i].length;j++)
			{
				if(processValues2[i][j]==0)
				{
					n1++;
					zeroFlag=true;
				}
			}
			
			if(zeroFlag&&n>n1)
			{
				//���ڱ�����һ��0�ĸ���
				n=n1;
				//��������0���е��±�
				index=i;
			}
		}

		return index;
	}
	/*
	 * @col ��
	 * @row ��
	 * 0��ʾ����0Ԫ��  -2��ʾ��������0Ԫ��
	 */
	public void change_1(int col,int row,Integer[][] processValues2) {
		//����һ��ֵΪ0�ĸ�ֵ-2
		for(int i=0;i<processValues2.length;i++)
		{
			if(processValues2[i][col]==0)
				processValues2[i][col]=-2;
		}
		//����һ�е�ֵΪ0��ֵΪ-2
		for(int i=0;i<processValues2.length;i++)
		{
			if(processValues2[row][i]==0)
				processValues2[row][i]=-2;
		}
		//-1��ʾ����0Ԫ��
		processValues2[row][col]=-1;
	}
	//��ȡһ�������ת�þ���
	public void tran(int[][] num)
	{
		for(int i=0;i<num.length;i++)
		{
			for(int j=0;j<num[i].length;j++)
			{
				num[j][i]=num[i][j];
			}
		}
	}
	//�ж��Ƿ�ﵽĿ��
	public boolean isMatchByRow(Integer[][] processValues2) {
		int onlyZero=0;//��ʾ������Ԫ�ظ���
		//�ж��Ƿ�ﵽĿ�ģ��ﵽ�����û�ﵽ����false	
		boolean flag=false;
		int len;
		//��0���ٵ���һ�н��б仯
		while((len=findLessZero(processValues2))!=-1)
		{
			for(int i=0;i<processValues2[len].length;i++)
			{
				if(processValues2[len][i]==0)
				{
					indexMap.put(len,i);
					onlyZero++;
					//�����к���������Ԫ�ظ�ֵΪ1
					change_1(i,len, processValues2);
					break;//����forѭ��
				}
			}
		}
		//û�дﵽ�������map
		if(onlyZero!=processValues2.length)
		{
			indexMap.clear();
		}else {
			flag=true;
		}
	
		return flag;
		
	}
}
