import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class WordWrapCellRenderer extends JLabel implements TableCellRenderer {
	
	private static String querystr;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(getHTML(value.toString()));
        setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        setPreferredSize(new Dimension(table.getColumnModel().getColumn(column).getWidth()-5, (int) ((value.toString().length() > 100) ? (value.toString().length()+value.toString().length()*0.3) : 100)));
        setVerticalAlignment(JLabel.TOP);
        table.setRowHeight(row, getPreferredSize().height);
        if (table.getRowHeight(row) != getPreferredSize().height) {
            table.setRowHeight(row, getPreferredSize().height);
        }
        return this;
    }
    
    private String getHTML(String string) {
    	String[] split = querystr.split(" ");
    	
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        String tmp = string;
        for(String s : split) {
        	tmp = tmp.replaceAll("(?i)\\b(" + s + ")\\b", "<font color='red'>$0</font>");
    	}
        sb.append(tmp);
        sb.append("</html>");
        return sb.toString();
    }

	public static void setVariable(String string) {
		querystr = string;
		
	}
}