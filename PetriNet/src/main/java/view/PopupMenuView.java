package view;

import com.mxgraph.model.mxCell;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PopupMenuView extends JPopupMenu {

    public PopupMenuView(Controller controller, mxCell object, List<String> agentsArray, Set<String> checked) {
        if (object.getId() != null) {
            JMenuItem deleteObj = new JMenuItem("Delete");
            deleteObj.addActionListener(e -> {
                controller.requestDelete(object);
            });
            add(deleteObj);
        }
        if(agentsArray!=null && !agentsArray.isEmpty()) {
            addSeparator();
            for (int i = 0; i < agentsArray.size(); ++i) {
                final String agentName = agentsArray.get(i);
                JCheckBoxMenuItem item = new JCheckBoxMenuItem(agentName);
                item.setSelected(checked.contains(agentName));
                add(item);
                item.addActionListener(e -> {
                    controller.addAgent(object, agentName, item.isSelected());
                });
            }
        }
    }
}
