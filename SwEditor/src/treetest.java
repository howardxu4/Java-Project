/**
*******************************************************************************
*
*   (treetest) Generated on Wed Jun 11 22:11:16 PDT 2003
*
*******************************************************************************
*/
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

import GuiMgr.SwUtil.*;
import ComMgr.*;

public class treetest implements IcallBack
{

/****************************************
*            constructor
*****************************************/
   public treetest () {
      ComMgr.getComMgr().addListener(ComMgr.USER05, this);
      // user's special init (before load)
   }

/****************************************
*            init run-time variables
*****************************************/
   private void init() {
      // user's variable init (after load)
   }

/****************************************
*         implements call back handler 
*****************************************/
   public Object callBack(int type, Object info) {
      if (type == NOTIFY) init();
      else if (type < UNKNOWN) 
         GuiMgr.SwUtil.runSafety(this, "process",
            new Object[]{new Integer(type), info}, null);
      else {   // user definded message handler
      }
      return null;
   }

/****************************************
*         system generated methods
*****************************************/
   public void process(int type, Object info) {
      Argument argv = (Argument)info;
      String name = (String)argv.getArgument(NAME);

   // the following useful info maybe referred on some case
   // Object obj = argv.getArgument(OBJECT);
   // int eType = ((Integer)argv.getArgument(TYPE)).intValue();
   // Object event = argv.getArgument(EVENT);
   // String value = (String)argv.getArgument(VALUE);

      switch (type) {
      case TREE :
         if (name.equals("myTest")) {
         // TODO: add your code for handling the TreeSelectionEvent on JTree [myTest]
         }
         break;
      default:
         break;
      }
   }

/****************************************
*            user's methods
*****************************************/

}
