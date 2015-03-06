
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class myTest1
{  public static void main(String[] args)
   {  JFrame frame = new DropTargetFrame();
      frame.show();
   }
}

class DropTargetFrame extends JFrame
{  public DropTargetFrame()
   {  setTitle("DropTarget");
      setSize(300, 300);
      addWindowListener(new WindowAdapter()
         {  public void windowClosing(WindowEvent e)
            {  System.exit(0);
            }
         } );

      Container contentPane = getContentPane();
      textArea
         = new JTextArea("Drop items into this text area.\n");

      new theDrop(textArea);
      contentPane.add(new JScrollPane(textArea), "Center");
   }
    JTextArea textArea;

	class theDrop extends myDrop {
		theDrop(Component cmp) {
		   super(cmp);
	}
	// overwrite
    public void processDrop(Object obj) {
      Transferable transferable = (Transferable)obj;
      DataFlavor[] flavors
         = transferable.getTransferDataFlavors();
	  textArea.append("Total number of data flavors are " + flavors.length + "\n");
      for (int i = 0; i < flavors.length; i++) {
 	     DataFlavor d = flavors[i];
		 
         textArea.append("MIME type=" + d.getMimeType() + "\n");

         try
         {  if (d.equals(DataFlavor.javaFileListFlavor))
            {  java.util.List fileList = (java.util.List)
                  transferable.getTransferData(d);
               Iterator iterator = fileList.iterator();
               while (iterator.hasNext())
               {  File f = (File)iterator.next();
                  textArea.append(f + "\n");
               }
            }
            else if (d.equals(DataFlavor.stringFlavor))
            {  String s = (String)
                  transferable.getTransferData(d);
               textArea.append(s + "\n");
            }
            else if (d.isMimeTypeEqual("text/plain"))
            {  String charset = d.getParameter("charset");
               InputStream in = (InputStream)
                  transferable.getTransferData(d);

               boolean more = true;
               int ch;
               if (charset.equals("ascii"))
               {  do
                  {  ch = in.read();
                     if (ch != 0 && ch != -1)
                        textArea.append("" + (char)ch);
                     else more = false;
                  } while (more);
               }
               else if (charset.equals("unicode"))
               {  boolean littleEndian = true;
                     // if no byte ordering mark, we assume
                     // Windows is the culprit
                  do
                  {  ch = in.read();
                     int ch2 = in.read();
                     if (ch != -1 && littleEndian)
                        ch = (ch & 0xFF) | ((ch2 & 0xFF) << 8);
                     if (ch == 0xFFFE)
                        littleEndian = false;
                     else if (ch != 0 && ch != -1)
                        textArea.append("" + (char)ch);
                     else more = false;
                  } while (more);
               }

               textArea.append("\n");
            }
         }
         catch(Exception e)
         {  textArea.append("Error: " + e + "\n");
         }
	  }
	}
			   
	}
}

