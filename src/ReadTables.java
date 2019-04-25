import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class ReadTables extends JFrame implements ActionListener{

	JButton button1,button2, button3, button4;
	ArrayList<String> list = new ArrayList<String>(), list2 = new ArrayList<String>(), list3 = new ArrayList<String>();
	//selectedColumns = new ArrayList<String>();
	Panel panel,panel2[],panel3[];
	JCheckBox[] c,c2;
	JTable table,newJointTable;
	String row[][];
	JScrollPane pane[],pane1,newPane;
	ArrayList<Integer> totalColumns = new ArrayList<Integer>();
	ArrayList<String> getSelectedColumns = new ArrayList<String>(), insertColumns = new ArrayList<String>(), getTableName = new ArrayList<String>(), getTable = new ArrayList<String>(), getColumn = new ArrayList<String>();
	String gcolumn[][];
	JLabel tableName[];
	int var;

	
	ReadTables(){
		
		button1 = new JButton("Show Tables");
		button1.setBackground(Color.BLACK);
		button1.setForeground(Color.WHITE);
		
		button2 = new JButton("Show Data");
		button2.setBackground(Color.BLACK);
		button2.setForeground(Color.WHITE);
		
		button3 = new JButton("Select Columns");
		button3.setBackground(Color.BLACK);
		button3.setForeground(Color.WHITE);
		

		button4 = new JButton("Form Table");
		button4.setBackground(Color.BLACK);
		button4.setForeground(Color.WHITE);


		setLayout(null);
		
		button1.setBounds(40,20,200,30);
		add(button1);
		
		button2.setBounds(270,20,200,30);
		add(button2);
		
		button3.setBounds(500,20,200,30);
		add(button3);

		
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);

		
		setSize(770,600);
		setVisible(true);
		setLocation(600,70);
		getContentPane().setBackground(Color.WHITE);
	}
	
	public void actionPerformed(ActionEvent ae){

		if(ae.getSource()==button1){
			
			try{
				conn c1 = new conn();
			
				//selecting the tables created after the given date
				String q = "SELECT object_name FROM dba_objects WHERE object_type = 'TABLE' AND owner = 'SYSTEM' AND owner = 'SYSTEM' AND created >= to_date('08-APR-2019','DD-MON-YYYY')";
				
				ResultSet rs = c1.s.executeQuery(q);
				 
				//Adding all the tables in the list
				while(rs.next()){		
					list.add(rs.getString("OBJECT_NAME"));
				}
				
				//creating check box to select tables
				panel = new Panel();
				c = new JCheckBox[list.size()];
				
				setLayout(null);
				
				for(int i = 0 ; i < list.size() ; i++){
					c[i] = new JCheckBox(list.get(i));
					panel.add(c[i]);
				}
				
				panel.setBackground(Color.WHITE);
				panel.setLayout(new GridLayout(list.size(),1));
				panel.setBounds(50,80,400,400);
				add(panel);
				
			
				this.repaint();
				setVisible(true);
		
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(ae.getSource()==button2){
			int count = 0;
			
			try{

				conn c1 = new conn();
				conn c2 = new conn();
				
				//calculating the count, number of tables selected
				for(int i = 0 ; i < list.size() ; i++){
					if(c[i].isSelected()){
						count++;
					}
				}

				//adding the selected tables in an array list "list2"
				for(int i = 0 ; i < list.size() ; i++){
					if(c[i].isSelected()){
						list2.add(list.get(i));
					}
				}
				
				int columnCount;
				String str[] = new String[list2.size()], column[];
				
				//to display no. of columns in a table
				gcolumn = new String[list.size()][];
				
				pane = new JScrollPane[list2.size()];
				tableName = new JLabel[list2.size()];
				
				for(int i = 0 ; i < list2.size() ; i++){
					
					
					tableName[i] = new JLabel(list2.get(i));
					
					//fetching the data of the table selected from the database
					str[i] = "select * from "+list2.get(i)+"";
					

					ResultSet rs = c1.s.executeQuery(str[i]);
					ResultSet rs1 = c2.s.executeQuery(str[i]);
					
					ResultSetMetaData rsmd = rs.getMetaData();
					
					//calculating the no. of columns in a table
					columnCount = rsmd.getColumnCount();
					
					//creating a array of the size of columns
					column = new String[columnCount];
					
					//adding the columns in an array list
					totalColumns.add(columnCount);
	
				
					gcolumn[i] = new String[columnCount];

					for (int j = 0 ; j < columnCount ; j++) {
						column[j] = rsmd.getColumnName(j+1);	
					}
					
					//adding the no. of columns in a particular table
					for(int j = 0 ; j < columnCount ; j++){
						gcolumn[i][j] = column[j];
					}

					
					int rowCount=0,cCount = 0, rCount = 0;	
					while(rs1.next()) {
						rowCount++;		
					}	

					//getting the data from the database into a 2-d array
					row = new String[rowCount][columnCount];
					while(rs.next()) {
						
						for (int counter = 1 ; counter <= columnCount ; counter++){
							row[rCount][cCount] = rs.getString(counter);
							cCount++;
						}
						cCount = 0;
						rCount++;
						
					}
					
					//putting the data into the table
					table = new JTable(row,column);
					table.setBackground(Color.WHITE);
				
					

					pane[i] = new JScrollPane(table);
		    		pane[i].setBackground(Color.WHITE);
					
					setLayout(null);
					
					repaint();
					
					panel.setVisible(false);
					
					if(i<2){
						tableName[i].setBounds(40+i*350,70,100,20);
						pane[i].setBounds(40+i*350,100,310,200);
					}else{
						tableName[i].setBounds(40+(i-2)*350,300,100,30);
						pane[i].setBounds(40+(i-2)*350,330,310,200);
					}
					
					pane[i].setBackground(Color.WHITE);
					add(tableName[i]);
					add(pane[i]); 
					setVisible(true);
					
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		
		}else if(ae.getSource()==button3){
			int count = 0;
			
			//calculating the total number of columns
			for(int i = 0 ; i < list2.size() ; i++){
				for(int j = 0 ; j < totalColumns.get(i) ; j++){
					count++;
				}
			}

			var = count;
			int counter = 0;
			
			//creating the check box for total no of columns
			c2 = new JCheckBox[count];
			
			panel3 = new Panel[list2.size()];
			
			for(int i = 0 ; i < list2.size() ; i++){
				
				panel2 = new Panel[totalColumns.get(i)];
				panel3[i] = new Panel();
				
				//adding the total columns of a table in a panel
				for(int j = 0 ; j < totalColumns.get(i) ; j++){
					c2[counter] = new JCheckBox(gcolumn[i][j]);
					insertColumns.add(gcolumn[i][j]);
					
					
					panel2[j] = new Panel();
					panel2[j].setLayout(new FlowLayout());
					
					panel2[j].setBounds(10,20*(i+50),130,150);
					panel2[j].setBackground(Color.WHITE);
					
					panel2[j].add(c2[counter]);
					panel3[i].add(panel2[j]);
					counter++;
				}
				
				
		
				
				panel3[i].setLayout(new GridLayout(totalColumns.get(i),1));
				panel3[i].setBackground(Color.WHITE);
				setSize(800,900);
				panel3[i].setBounds(50+i*150,650,130,150);
				add(panel3[i]);
				
			
			}
			setLayout(null);
			
			//button 4 -- started
			class handler1 implements ActionListener{
				public void actionPerformed(ActionEvent e){
					
					System.out.println("Clalled");
					
					table.setVisible(false);
					
					panel.setVisible(false);
					button1.setVisible(false);
					button2.setVisible(false);
					button3.setVisible(false);
					button4.setVisible(false);
					
					for(int i = 0 ; i < list2.size() ; i++){
						panel3[i].setVisible(false);
						pane[i].setVisible(false);
						tableName[i].setVisible(false);
					}
				
			
					
					int count1 = 0, k = 0;
					System.out.println("--------------------------------------------------\n ");
					for(int i = 0 ; i < var ; i++){
						if(c2[i].isSelected()){
							System.out.println("insert columns : "+insertColumns.get(i));
							getSelectedColumns.add(insertColumns.get(i));
							k++;
						}
					}
					
					
					int k1 = 0, k2=0;
					boolean flag = false;
					for(int i = 0 ; i < list2.size() ; i++){
						for(int j = 0 ; j < totalColumns.get(i) ; j++){
							if(getSelectedColumns.get(k1).equals(insertColumns.get(k2))){
								
								getTable.add(list2.get(i));
								getColumn.add(getSelectedColumns.get(k1));
								System.out.println("selected colums are : "+getSelectedColumns.get(k1));
								System.out.println(list2.get(i));
								++k1;
							}
							if(getSelectedColumns.size()==k1){
								flag = true;
								break;
							}
							k2++;
						}
						if(flag){
							break;
						}
						
					}
					
					String newTable[][] = new String[getColumn.size()][20];
					String newColumn[] = new String[getColumn.size()];
					int cCount = 0, rCount = 0, countCol = 0;
					try{
						conn c1 = new conn();
						
						
						for(int i = 0 ; i < getTable.size() ; i++){ 
							for(int j = 0 ; j < getColumn.size() ; j++){
								
								
								System.out.println("getTable.size() : "+getTable.size());  //4
								System.out.println("getColumnSize() : "+getColumn.size()); //4
								
								
								
								String q = "select "+getColumn.get(j)+" from "+getTable.get(i)+"";
								
								System.out.println(q);

								ResultSet rs = c1.s.executeQuery(q);
								
								System.out.println("After result set : "+rs);
								
								while(rs.next()){
									
									System.out.println("Inside while");
									
							
										newTable[rCount][cCount] = rs.getString(1);
										cCount++;
										System.out.println("After while");
								}
								cCount = 0;
								rCount++;
								newColumn[countCol] = getColumn.get(j);
								countCol++;
								
							}
						}
								
								
						newJointTable = new JTable(newTable ,newColumn);
						newJointTable.setBackground(Color.WHITE);
					
						

						newPane = new JScrollPane(newJointTable);
			    		newPane.setBackground(Color.WHITE);
						
						setLayout(null);
						add(newPane);
						repaint();
						
						
					}catch(Exception ex){
						ex.printStackTrace();
					}

					setSize(600,500);
					repaint();
					setVisible(true);
				}
			}

			button4.setBounds(550,820,150,30);
			add(button4);
			handler1 thehandler1 =new handler1();
			
			button4.addActionListener(thehandler1);

			
			this.repaint();
			setVisible(true);

		}
	}
	
	public static void main(String[] args){
		new ReadTables();
	}
}