import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField taskInputField;

    public MainGUI() {
        setTitle("My To-Do List");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        taskInputField = new JTextField();
        JButton addButton = new JButton("Add List");

        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(inputPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton completeButton = new JButton("Complete List");
        JButton notcompleteButton = new JButton("Not Complete List");
        JButton editButton = new JButton("Edit List");
        JButton deleteButton = new JButton("Delete List");

        buttonPanel.add(completeButton);
        buttonPanel.add(notcompleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String name = taskInputField.getText().trim();
            if (!name.isEmpty()) {
                TaskManager.add(name);
                taskInputField.setText("");
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(null, "Please input list name first!", " Notification", JOptionPane.WARNING_MESSAGE);
            }
        });

        completeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                TaskManager.complete(selectedIndex + 1);
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(null, "Please select list to complete");
            }
        });

        notcompleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                TaskManager.notcomplete(selectedIndex + 1);
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(null, "Please select list to not complete");
            }
        });

        editButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String currentName = TaskManager.tasks.get(selectedIndex).taskName;
                String newName = JOptionPane.showInputDialog(null, "Edit list name:", currentName);

                while (newName != null && newName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please input a new name again!", "Notification",
                            JOptionPane.WARNING_MESSAGE);
                    newName = JOptionPane.showInputDialog(null, "Edit list name:", currentName);
                }

                if (newName != null) {
                    TaskManager.edit(selectedIndex + 1, newName.trim());
                    refreshTaskList();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select list to edit");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this list?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    TaskManager.delete(selectedIndex + 1);
                    refreshTaskList();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Please select list to delete");
            }
        });

        refreshTaskList();
    }

    private void refreshTaskList() {
        listModel.clear();
        for (int i = 0; i < TaskManager.tasks.size(); i++) {
            Task task = TaskManager.tasks.get(i);
            String statusStr = task.status ? "(Completed)" : "(Not Completed)";
            listModel.addElement((i + 1) + ". " + task.taskName + " " + statusStr);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}