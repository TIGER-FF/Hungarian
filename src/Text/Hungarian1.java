package Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Hungarian1 {

	private Integer[][] originalValues;//存储原始的成本数组
	private Integer[][] processValues;//用于变化的成本数组
	List<Integer> rowIndex=new ArrayList<Integer>();//存放没有-1的行下标
	List<Integer> colIndex=new ArrayList<Integer>();//存放没有-1的行中有-2的下标
	private Map<Integer, Integer> indexMap=new HashMap<Integer, Integer>();//用于存储最后的坐标
	//构造函数用于刚开始将原始数据存储在originalValues，并存储在processValues中用于变化
	//这块process存储需要用到clone函数
	public  Hungarian1(Integer[][] values) {
		//因为输出的为阶矩阵所以行和列一样
		int len=values.length;
		originalValues=values;//原始数据，将orginalValues数组指向原来的数组
		processValues=MyClone(values);//得到一个新的数组与原始数组没有地址关联
		boolean flag=false;//表示最后indexMap中存储是行列，还是列行（true:列行）
		//1、对数组的每一行减去每行的最小值
		for(int i=0;i<len;i++)
		{
			//得到每行的最小值
			int min=minNum(processValues[i]);
			for(int j=0;j<len;j++)
			{
				//对每一行的数减去最小值
				processValues[i][j]-=min;
				
			}
		
		}
		//2、对数组的每一列减去每一列的最小值
		for(int i=0;i<len;i++)
		{
			//新建一个临时一维数组用来存储每一列
			Integer[] lie=new Integer[len];
			for(int j=0;j<len;j++)
			{
				//取出每一列的数组
				lie[j]=processValues[j][i];
			}
			//得到每一列的最小值
			int min=minNum(lie);
			for(int j=0;j<len;j++)
			{
				//对每一列的值减去自己列的最小值
				processValues[j][i]-=min;
			}
		}
		//当进行到之一步时候有的成本矩阵已经达到了我们想要的目的了，需要进行判断是否达到
		while(!isMatchByRow(processValues))//没有达到目的
		{
			//3、画线对行里没有-1的进行画线，对画线行里有-2的列画线，对画线列中有-1的画线
			//不需要画线了，只需要找出要最小值的集合，求出最小值就行
			int min=PaintLine(processValues);
			
			
			//还原数组
			for(int i=0;i<processValues.length;i++)
			{
				for(int j=0;j<processValues[i].length;j++)
				{
					if(processValues[i][j]==-1||processValues[i][j]==-2)
						processValues[i][j]=0;
				}
			}//end for
			//需要判断画线数是否等于lenght等于也证明符合
			if(processValues.length==(processValues.length-rowIndex.size()+colIndex.size()))
			{
				//按列进行判断将矩阵转置
				if(isMatchByRow(processValues))
				{
					flag=true;
					break;
				}
					
			}
			//对大对号的行减去min
			for(int i:rowIndex)
			{
				for(int j=0;j<processValues[i].length;j++)
				{
						processValues[i][j]-=min;				
				}
			}//end  for(int i:rowIndex)
			//对列加上min
			for(int i:colIndex)
			{
				for(int j=0;j<processValues.length;j++)
				{
						processValues[j][i]+=min;				
				}
			}//end  for(int i:rowIndex)
			//清空rowIndex和colIndex
			rowIndex.clear();
			colIndex.clear();
		}//end while
		int count=0;
		if(flag)//flag为true为key--列      value----行
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
	//画线
	
	
	public int findLessZeroByCol(Integer[][] processValues2) {
		//表示每一列0的个数
		//初始化为最大
		int n=processValues2.length;
		int index=-1;//表示哪一个0最少的行标
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
				//用于保存上一行0的个数
				n=n1;
				//保存最少0的行的下标
				index=i;
			}
		}

		return index;
	}

	public int PaintLine(Integer[][] processValues2) {
		//-3表示画线
		
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
		//从没有画线的数字中找出最小值
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
	//返回一个新的数组改变这个数组但是原始数据不会跟着变化而变化
	public Integer[][] MyClone(Integer[][] values) {
		Integer[][] num_clone=new Integer[values.length][values[0].length];//新建一个数组
		for(int i=0;i<values.length;i++)
		{
			//这是运功clone函数
			num_clone[i]=values[i].clone();
			/*
				//这是一种数组copy的方法等与for()循环给数组每一个赋值
				System.arraycopy(num[i], 0, num_clone[i], 0, num[i].length);
			*/
		}
		return num_clone;
	}
	//查找一个一维数组的最小数
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
	//寻找数组哪一行零个数最小并返回行数
	public int findLessZero(Integer[][] processValues2) {
		//表示每一行0的个数
		//初始化为最大
		int n=processValues2.length;
		int index=-1;//表示哪一个0最少的行标
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
				//用于保存上一行0的个数
				n=n1;
				//保存最少0的行的下标
				index=i;
			}
		}

		return index;
	}
	/*
	 * @col 列
	 * @row 行
	 * 0表示独立0元素  -2表示被划掉的0元素
	 */
	public void change_1(int col,int row,Integer[][] processValues2) {
		//对这一列值为0的赋值-2
		for(int i=0;i<processValues2.length;i++)
		{
			if(processValues2[i][col]==0)
				processValues2[i][col]=-2;
		}
		//对这一行的值为0赋值为-2
		for(int i=0;i<processValues2.length;i++)
		{
			if(processValues2[row][i]==0)
				processValues2[row][i]=-2;
		}
		//-1表示独立0元素
		processValues2[row][col]=-1;
	}
	//获取一个数组的转置矩阵
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
	//判断是否达到目的
	public boolean isMatchByRow(Integer[][] processValues2) {
		int onlyZero=0;//表示独立零元素个数
		//判断是否达到目的，达到输出，没达到返回false	
		boolean flag=false;
		int len;
		//从0最少的那一行进行变化
		while((len=findLessZero(processValues2))!=-1)
		{
			for(int i=0;i<processValues2[len].length;i++)
			{
				if(processValues2[len][i]==0)
				{
					indexMap.put(len,i);
					onlyZero++;
					//把这行和这列所以元素赋值为1
					change_1(i,len, processValues2);
					break;//跳出for循环
				}
			}
		}
		//没有达到清空坐标map
		if(onlyZero!=processValues2.length)
		{
			indexMap.clear();
		}else {
			flag=true;
		}
	
		return flag;
		
	}
}
