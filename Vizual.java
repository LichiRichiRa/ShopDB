package store;

import java.awt.*; 
import java.awt.event.*;
import java.io.FileNotFoundException;

import javax.persistence.*; 
import javax.swing.*; 
import javax.swing.table.DefaultTableModel; 
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;

public class Visual {
	public final static Logger logger = Logger.getLogger(Visual.class);
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
	EntityManager em = emf.createEntityManager();
	private JDialog dialog;
	private JFrame windowApp;
	private int size = 20;
	private JToolBar toolBar; 
	private DefaultTableModel modelP;
	private DefaultTableModel modelS;
	
	private JButton info; 
	private JButton add; 
	private JButton del;
	private JButton change;
	private JButton edit;
	private JButton save;
	
	private JScrollPane scroll; 
	private JTable STable; 
	private JTable PTable; 
	private JComboBox kind; 
	
	String [] InfoShop = { "Supreme", "Vaneev Andrey", "London 2/3 Peter Street", "grocery shop"};
    Shop prd = new Shop();
	public void show() throws InputException, ZeroException{
		windowApp = new JFrame("Store"); 
		windowApp.setSize(700, 500); 
		windowApp.setLocation(100, 100); 
		windowApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		info = new JButton(new ImageIcon(new ImageIcon("pictures/info.png").getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH))); 
		add = new JButton(new ImageIcon(new ImageIcon("pictures/plus.png").getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH))); 
		del = new JButton(new ImageIcon(new ImageIcon("pictures/trash.png").getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH)));
		change = new JButton(new ImageIcon(new ImageIcon("pictures/pen.png").getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH)));
		edit = new JButton(new ImageIcon(new ImageIcon("pictures/shop.png").getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH)));
		save = new JButton(new ImageIcon(new ImageIcon("pictures/save.png").getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH)));
		
		info.setActionCommand("open"); 
		add.setActionCommand("add"); 
		del.setActionCommand("delete"); 
		change.setActionCommand("buy");
		edit.setActionCommand("edit");
		save.setActionCommand("save");
		
		info.setToolTipText("Информация о магазине"); 
		add.setToolTipText("Добавить запись"); 
		del.setToolTipText("Удалить запись"); 
		change.setToolTipText("Изменить запись");
		edit.setToolTipText("Изменить информацию о магазине");
		save.setToolTipText("Загрузить отчет");
		
		toolBar = new JToolBar("Панель инструментов"); 
		toolBar.add(info); 
		toolBar.add(add); 
		toolBar.add(del); 
		toolBar.add(change);
		toolBar.add(edit);
		toolBar.add(save);

		
		windowApp.setLayout(new BorderLayout()); 
		windowApp.add(toolBar, BorderLayout.NORTH);
		String [][] data =  null;
		String [] columnsProducts = {"Id", "Название", "Цена", "Количество"};
		String [] columnsSellers = {"Id", "Имя", "Зарплата"};
		modelP= new DefaultTableModel(data, columnsProducts); 
		PTable = new JTable(modelP); 
		scroll = new JScrollPane(PTable);
		modelS= new DefaultTableModel(data, columnsSellers); 
		STable = new JTable(modelS); 
		windowApp.add(scroll, BorderLayout.CENTER);
		kind = new JComboBox(new String[]{"Продукты", "Продавцы"}); 	 
		JPanel filterPanel = new JPanel(); 
		filterPanel.add(kind); 
		//filterPanel.add(filter); 
		windowApp.add(filterPanel, BorderLayout.SOUTH); 
		windowApp.setVisible(true);
		
		kind.addActionListener(event -> {if (kind.getSelectedItem() == "Продукты") {
											scroll.setViewportView(PTable);
											windowApp.add(scroll, BorderLayout.CENTER);
										}
										else{
											scroll.setViewportView(STable);
											windowApp.add(scroll, BorderLayout.CENTER);
										}
		}
		);
		logger.info("App start");
		OldDB();
		add.addActionListener(event -> {if (kind.getSelectedItem() == "Продукты") addProductFunc(); else addSellerFunc();});
		del.addActionListener(event -> {if (kind.getSelectedItem() == "Продукты") delProductFunc();else delSellerFunc();});
		change.addActionListener(event -> {if (kind.getSelectedItem() == "Продукты") editProductFunc();else editSellerFunc();});
		info.addActionListener(event -> {ShopShow(InfoShop);});
		edit.addActionListener(event -> {ChangeInfo(InfoShop);});
		save.addActionListener(event -> {if (kind.getSelectedItem() == "Продукты")
			try {
				Report.reportProducts();
				reportDone();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			try {
				Report.reportSellers();
				reportDone();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}});
	}

	private void addProductFunc(){ 
		JPanel addWin = createAddWinProduct(); 
		dialog = new JDialog(windowApp, "Добавление записи", true); 
		JButton addBtn = new JButton("Добавить"); 
		ActionListener actionPressAdd = new ActionListener() {
		@Override 
		public void actionPerformed(ActionEvent e) { 
			Component[] components = addWin.getComponents(); 
			try{
				AddProduct(((JTextField)components[1]).getText(), ((JTextField)components[3]).getText(), ((JTextField)components[5]).getText(), false, 1); 			
				JOptionPane.showMessageDialog(new JDialog(windowApp, "", true), "<html>Продукт добавлен"); 
				dialog.dispose(); 
				} 
			catch(InputException exp){ 
				JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),exp.getMessage());
				} catch (ZeroException e1) {
					JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),e1.getMessage());
			} 
			}
		};
			
		addBtn.addActionListener(actionPressAdd); 
		dialog.setResizable(false);

		dialog.setPreferredSize(new Dimension(400, 250)); 
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		dialog.setLocation(250, 250);
		dialog.add(addWin, BorderLayout.NORTH); 
		dialog.add(addBtn, BorderLayout.SOUTH); 
		dialog.pack(); 
		dialog.setVisible(true); 
		
		logger.info("Added Product");
		}
	
	private void addSellerFunc(){ 
		JPanel addWin = createAddWinSeller(); 
		dialog = new JDialog(windowApp, "Добавление записи", true); 
		JButton addBtn = new JButton("Добавить"); 
		ActionListener actionPressAdd = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) { 
				Component[] components = addWin.getComponents(); 
				try { 
					AddSeller(((JTextField)components[1]).getText(), ((JTextField)components[3]).getText(), false, 1); 
					JOptionPane.showMessageDialog(new JDialog(windowApp, "", true), "<html>Продавец добавлен"); 
					dialog.dispose();
				}
				catch(InputException exp) {
					JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),exp.getMessage());
					} catch (ZeroException e1) {
						JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),e1.getMessage());
				}
				}
		};
		addBtn.addActionListener(actionPressAdd); 
		dialog.setResizable(false);

		dialog.setPreferredSize(new Dimension(400, 250)); 
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		dialog.setLocation(250, 250);
		dialog.add(addWin, BorderLayout.NORTH); 
		dialog.add(addBtn, BorderLayout.SOUTH); 
		dialog.pack(); 
		dialog.setVisible(true); 
		logger.info("Added Seller");
		}
	
	private JPanel createAddWinProduct(){ 
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(0, 2, 15, 20)); 
		panel.add(new JLabel("Название продукта: ")); 
		panel.add(new JTextField("",45));  
		panel.add(new JLabel("Цена: "));
		panel.add(new JTextField( "",45)); 
		panel.add(new JLabel("Количество: ")); 
		panel.add(new JTextField("",45)); 
		return panel; 
		}
	
	private JPanel createAddWinSeller(){ 
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(0, 2, 15, 20)); 
		panel.add(new JLabel("Имя: ")); 
		panel.add(new JTextField("",45));  
		panel.add(new JLabel("Зарплата: "));
		panel.add(new JTextField( "",45));  
		return panel; 
		}
	
	public boolean AddProduct(String new_name, String new_price_s, String new_quantity_s, boolean old, int id)throws InputException, ZeroException {
		int new_price, new_quantity; 
		try{ 
			new_price = Integer.parseInt(new_price_s); 
			new_quantity = Integer.parseInt(new_quantity_s);
			}
		catch (NumberFormatException | NullPointerException exp){ throw new InputException(); }  
		if (new_price <= 0 | new_quantity < 0 | new_name.length() > 45) throw new InputException();
		if (new_name.length() == 0) throw new ZeroException();
		if (!em.getTransaction().isActive()) em.getTransaction().begin();
		Product new_product = new Product();
		new_product.SetName(new_name);
		new_product.SetPrice(new_price);
		new_product.SetQuantity(new_quantity);;
		if (!old) {
			em.persist(new_product);
	        em.getTransaction().commit();	
		}
		else {
			new_product.SetID(id);
		}
        modelP.addRow(new Object[] {new_product.GetID(),new_name, new_price, new_quantity});
        return true;
	}
	public boolean AddSeller(String new_name, String new_salary_s, boolean old, int id) throws InputException, ZeroException {
		int new_salary; 
		try{  
			new_salary = Integer.parseInt(new_salary_s); 
			}
		catch (NumberFormatException | NullPointerException exp){throw new InputException(); }  
		if (new_salary <= 0 ) throw new InputException();
		if (new_name.length() == 0) throw new ZeroException();
		if (!em.getTransaction().isActive()) em.getTransaction().begin();
		Seller new_seller = new Seller();
		new_seller.SetName(new_name);
		new_seller.SetSalary(new_salary);
		if (!old) {
			em.persist(new_seller);
	        em.getTransaction().commit();	
		}
		else {
			new_seller.SetID(id);
		}
        modelS.addRow(new Object[] {new_seller.GetID(),new_name, new_salary});
        return true;
	}
	
	private void delProductFunc(){ 
		if (PTable.getSelectedRow() > -1) {
			if (!em.getTransaction().isActive()) em.getTransaction().begin(); 
			Product prd = em.find(Product.class, Integer.parseInt(PTable.getValueAt(PTable.getSelectedRow(), 0).toString())); 
			em.remove(prd); 
			em.getTransaction().commit(); 
			modelP.removeRow(PTable.getSelectedRow()); 
			modelP.fireTableDataChanged(); 
			logger.info("Delete Product");
			}
		}
	private void delSellerFunc(){ 
		if (STable.getSelectedRow() > -1) {
			if (!em.getTransaction().isActive()) em.getTransaction().begin(); 
			Seller prd = em.find(Seller.class, Integer.parseInt(STable.getValueAt(STable.getSelectedRow(), 0).toString())); 
			em.remove(prd); 
			em.getTransaction().commit(); 
			modelS.removeRow(STable.getSelectedRow()); 
			modelS.fireTableDataChanged(); 
			logger.info("Delete Seller");
			}
		}
	
	private JPanel createEditWinProduct(Product prd){ 
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(0, 2, 15, 20)); 
		panel.add(new JLabel("Название продукта: ")); 
		panel.add(new JTextField(prd.GetName(),45));  
		panel.add(new JLabel("Цена: "));
		panel.add(new JTextField( Integer.toString(prd.GetPrice()),45)); 
		panel.add(new JLabel("Количество: ")); 
		panel.add(new JTextField(Integer.toString(prd.GetQuantity()),45)); 
		return panel; 
		}
	
	private JPanel createEditWinSeller(Seller prd){ 
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(0, 2, 15, 20)); 
		panel.add(new JLabel("Имя: ")); 
		panel.add(new JTextField(prd.GetName(),45));  
		panel.add(new JLabel("Зарплата: "));
		panel.add(new JTextField( Integer.toString(prd.GetSalary()),45));  
		return panel; 
		}
	
	private void editProductFunc(){ 
		if (PTable.getSelectedRow() > -1) {
			if (!em.getTransaction().isActive()) em.getTransaction().begin(); 
			Product prd = em.find(Product.class, Integer.parseInt(PTable.getValueAt(PTable.getSelectedRow(), 0).toString()));  
			JPanel Win = createEditWinProduct(prd);
			dialog = new JDialog(windowApp, "Изменить информацию о товаре", true); 
			JButton editBtn = new JButton("Принять изменения");
			ActionListener actionPressEdit = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				Component[] components = Win.getComponents(); 
					try {
						editProductNote( prd, ((JTextField)components[1]).getText(), ((JTextField)components[3]).getText(), ((JTextField)components[5]).getText()); 
						JOptionPane.showMessageDialog(new JDialog(windowApp, "", true), "<html>Информация изменена"); 
						dialog.dispose(); 
						} 
					catch(InputException exp){
						JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),exp.getMessage()); 
					} catch (ZeroException e1) {
						JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),e1.getMessage());
					}
				}
			};
			editBtn.addActionListener(actionPressEdit); 
			dialog.setResizable(false);
			dialog.setPreferredSize(new Dimension(600, 300));
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setLocation(250, 250); 
			dialog.add(editBtn, BorderLayout.SOUTH); 
			dialog.add(Win, BorderLayout.NORTH); 
			dialog.pack(); 
			dialog.setVisible(true); 
			em.persist(prd);  
			PTable.setValueAt(prd.GetPrice(), PTable.getSelectedRow(), 2);
			PTable.setValueAt(prd.GetQuantity(), PTable.getSelectedRow(), 3); 
			modelP.fireTableCellUpdated(PTable.getSelectedRow(), 2);
			modelP.fireTableCellUpdated(PTable.getSelectedRow(), 3);
			
			logger.info("Edit Product");
		}
		
		
	}
	private boolean editProductNote(Product pr, String name, String price_s, String quantity_s) throws InputException, ZeroException{ 
		int price, quantity; 
		try{ 
			price = Integer.parseInt(price_s);
			quantity = Integer.parseInt(quantity_s);
			} 
		catch (NumberFormatException | NullPointerException exp){throw new InputException();} 
		if (price <= 0 | quantity <0 | name.length() > 45) throw new InputException();
		if (name.length() == 0) throw new ZeroException();
	if (!em.getTransaction().isActive()) em.getTransaction().begin();
	
	pr.SetName(name); 
	pr.SetPrice(price);
	pr.SetQuantity(quantity); 
	em.persist(pr); 
	em.getTransaction().commit();
	
	PTable.setValueAt(pr.GetName(), PTable.getSelectedRow(), 1);
	PTable.setValueAt(pr.GetPrice(), PTable.getSelectedRow(), 2);
	PTable.setValueAt(pr.GetQuantity(), PTable.getSelectedRow(), 3);
	
	modelP.fireTableCellUpdated(PTable.getSelectedRow(), 1);
	modelP.fireTableCellUpdated(PTable.getSelectedRow(), 2); 
	modelP.fireTableCellUpdated(PTable.getSelectedRow(), 3);
	

	return true;
	}
	
	public void editSellerFunc(){ 
		if (STable.getSelectedRow() > -1) {
			if (!em.getTransaction().isActive()) em.getTransaction().begin(); 
			Seller prd = em.find(Seller.class, Integer.parseInt(STable.getValueAt(STable.getSelectedRow(), 0).toString()));  
			JPanel Win = createEditWinSeller(prd);
			dialog = new JDialog(windowApp, "Изменить информацию о продавце", true); 
			JButton editBtn = new JButton("Принять изменения");
			ActionListener actionPressEdit = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				Component[] components = Win.getComponents(); 
				try {
						editSellerNote( prd, ((JTextField)components[1]).getText(), ((JTextField)components[3]).getText()); 
						JOptionPane.showMessageDialog(new JDialog(windowApp, "", true), "<html>Информация изменена"); 
						dialog.dispose(); 
						} 
					catch(InputException exp){
						JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),exp.getMessage()); 
					} catch (ZeroException e1) {
						JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),e1.getMessage());
					}
				}
			};
			editBtn.addActionListener(actionPressEdit); 
			dialog.setResizable(false);
			dialog.setPreferredSize(new Dimension(600, 300));
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setLocation(250, 250); 
			dialog.add(editBtn, BorderLayout.SOUTH); 
			dialog.add(Win, BorderLayout.NORTH); 
			dialog.pack(); 
			dialog.setVisible(true); 
			em.persist(prd); 
			STable.setValueAt(prd.GetSalary(), STable.getSelectedRow(), 2);
			modelS.fireTableCellUpdated(STable.getSelectedRow(), 2);
			
			logger.info("Edit Seller");
		}	
	}
	
	public boolean editSellerNote(Seller pr, String name, String salary_s) throws InputException, ZeroException{ 
		int salary; 
		try{ 
			salary = Integer.parseInt(salary_s);
			} 
		catch (NumberFormatException | NullPointerException exp){throw new InputException(); } 
		if (salary <= 0 | name.length() > 45) throw new InputException();
		if (name.length() == 0) throw new ZeroException();
	if (!em.getTransaction().isActive()) em.getTransaction().begin();
	
	pr.SetName(name); 
	pr.SetSalary(salary);
	em.persist(pr); 
	em.getTransaction().commit();
	
	STable.setValueAt(pr.GetName(), STable.getSelectedRow(), 1);
	STable.setValueAt(pr.GetSalary(), STable.getSelectedRow(), 2);
	
	modelS.fireTableCellUpdated(STable.getSelectedRow(), 1);
	modelS.fireTableCellUpdated(STable.getSelectedRow(), 2); 
	
	return true;
	}
	
	private void ChangeInfo(String[] info) {
		if (!em.getTransaction().isActive()) em.getTransaction().begin(); 
		JPanel Win = createEditWin(prd);
		dialog = new JDialog(windowApp, "Изменить информацию о магазине", true); 
		JButton editBtn = new JButton("Принять изменения");
		ActionListener actionPressEdit = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Component[] components = Win.getComponents(); 
				boolean check = editShopNode( prd, ((JTextField)components[1]).getText(), ((JTextField)components[3]).getText(), ((JTextField)components[5]).getText(), ((JTextField)components[7]).getText()); 
				if (check){ 
					JOptionPane.showMessageDialog(new JDialog(windowApp, "", true), "<html>Информация изменена"); 
					dialog.dispose(); } 
				else JOptionPane.showMessageDialog(new JDialog(windowApp, "", true),"<html>Ошибка изменения, введите все поля корректно"); 
			}
		};
		editBtn.addActionListener(actionPressEdit); 
		dialog.setResizable(false);
		dialog.setPreferredSize(new Dimension(600, 300));
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setLocation(250, 250); 
		dialog.add(editBtn, BorderLayout.SOUTH); 
		dialog.add(Win, BorderLayout.NORTH); 
		dialog.pack(); 
		dialog.setVisible(true); 
		em.persist(prd); 
		
		logger.info("Info changed");
	}
	
	private JPanel createEditWin(Shop prd){ 
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(0, 2, 15, 20)); 
		panel.add(new JLabel("Название магазина: ")); 
		panel.add(new JTextField(prd.GetStoreName(),45));  
		panel.add(new JLabel("Директор: "));
		panel.add(new JTextField(prd.GetDirector(),45)); 
		panel.add(new JLabel("Адрес: "));
		panel.add(new JTextField(prd.GetAdress(),45)); 
		panel.add(new JLabel("Специализация: "));
		panel.add(new JTextField(prd.GetSpecialization(),45)); 
		return panel; 
		}
	
	private boolean editShopNode(Shop pr, String name, String director, String adress, String specialization){ 
	if (!em.getTransaction().isActive()) em.getTransaction().begin();
	
	pr.SetStoreName(name); 
	InfoShop[0] = name;
	pr.SetDirector(director);
	InfoShop[1] = director;
	pr.SetAdress(adress); 
	InfoShop[2] = adress;
	pr.SetSpecialization(specialization);
	InfoShop[3] = specialization;
	em.persist(pr); 
	//em.getTransaction().commit(); 
	
	return true;
	}
	
	void ShopShow(String [] InfoShop) {
		dialog = new JDialog(windowApp, "Информация о магазине", true);
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(0, 2, 15, 20)); 
		panel.add(new JLabel("Название магазина: " + InfoShop[0] +"\n")); 
		panel.add(new JLabel("Директор: " + InfoShop[1]));
		panel.add(new JLabel("Адрес: " + InfoShop[2]));
		panel.add(new JLabel("Специализация: " + InfoShop[3]));
		dialog.setResizable(false);
		dialog.setPreferredSize(new Dimension(600, 100));
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setLocation(250, 250); 
		dialog.add(panel, BorderLayout.NORTH); 
		dialog.pack(); 
		dialog.setVisible(true); 
	}
	
	void OldDB() throws InputException, ZeroException {
		if (!em.getTransaction().isActive()) em.getTransaction().begin(); 
		List<Product> dataProducts = em.createQuery("SELECT products FROM store.Product products WHERE id > 0").getResultList();
		List<Seller> dataSellers = em.createQuery("SELECT sellers FROM store.Seller sellers WHERE id > 0").getResultList();
		if (dataProducts != null) {
			for (Product product : dataProducts) {
				AddProduct(product.GetName(), Integer.toString(product.GetPrice()),Integer.toString(product.GetQuantity()), true, product.GetID());
			}
		}
		if (dataSellers != null) {
			for (Seller seller : dataSellers) {
				AddSeller(seller.GetName(), Integer.toString(seller.GetSalary()), true, seller.GetID());
			}
		}	
		logger.info("Data loaded");
	}
	private void reportDone() {
		JOptionPane.showMessageDialog(new JDialog(windowApp, "", true), "<html>Отчет создан");  
	}
}
