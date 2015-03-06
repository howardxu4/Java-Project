
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class myTest
{  public static void main(String[] args)
   {  
	  JFrame frame = new DragSourceFrame();
      frame.show();
   }
}

class DragSourceFrame extends JFrame
{  public DragSourceFrame()
   {  
  	  GuiMgr.SwUtil.setContext(this);
 
	  setTitle("DragSourceTest");
      setSize(300, 200);
	  this.addWindowListener(GuiMgr.SwClbk.getCallBack());
	  this.addComponentListener(GuiMgr.SwClbk.getCallBack());
	  
      Container contentPane = getContentPane();
      File f = new File(".").getAbsoluteFile();
      File[] files = f.listFiles();
      model = new DefaultListModel();
      for (int i = 0; i < files.length; i++)
         model.addElement(files[i]);
      fileList = new JList(model);
      contentPane.add(new JScrollPane(fileList), "Center");
      contentPane.add(new JLabel("Drag files from this list"),
         "North");
	  JButton btn = new JButton("try me");
	  btn.addMouseListener(new MouseAdapter() {
		public void mousePressed( MouseEvent e ) {
			openPopup(e);
		}
		});
	  contentPane.add(btn, "East");
	  pop = new myPopup(btn, true);
	  
	  new theDrag(fileList);
   }
   private JList fileList;
   private DefaultListModel model;
   private myPopup pop;
   
   void openPopup(MouseEvent e) {
		PopupFactory factory = PopupFactory.getSharedInstance();
		Container cn = (Container)e.getSource(); 
		Point p = new Point(0,cn.getSize().height);
		pop.showPopup(cn, p); 
   }
	class theDrag extends myDrag {
		theDrag(Component cmp) {
			super(cmp);
		}
		// overwrite
		public void getDraggedValues() {
			super.setDraggedValues(fileList.getSelectedValues());
		}
		public void processDrop(Object obj) {
			System.out.println(" ==> " + obj.toString());
            model.removeElement(obj);
		}
		
	}
}