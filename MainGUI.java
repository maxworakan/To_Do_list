import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField taskInputField;

    public MainGUI() {
        // ตั้งค่าหน้าต่างหลัก (JFrame)
        setTitle("To-Do List");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- ส่วนการแสดงรายการ Task (Center) ---
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // --- ส่วนกรอกข้อมูลเพิ่ม Task (North) ---
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        taskInputField = new JTextField();
        JButton addButton = new JButton("Add List");

        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(inputPanel, BorderLayout.NORTH);

        // --- ส่วนปุ่มจัดการ Task (South) ---
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

        // --- Event Listeners (การทำงานของปุ่มต่าง ๆ) ---

        // 1. เพิ่ม Task (Add)
        addButton.addActionListener(e -> {
            String name = taskInputField.getText().trim();
            if (!name.isEmpty()) {
                TaskManager.add(name); // เรียกใช้ TaskManager เดิม
                taskInputField.setText("");
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(null, "Please input menu!", " Notification", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 2. ทำเครื่องหมายเสร็จสิ้น (Complete)
        completeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                // TaskManager อิง index เริ่มที่ 1
                TaskManager.complete(selectedIndex + 1);
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(null, "Please select menu Successful");
            }
        });

        // 3. ไม่ทำเครื่องหมายเสร็จสิ้น (Not Complete)
        notcompleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                // TaskManager อิง index เริ่มที่ 1
                TaskManager.notcomplete(selectedIndex + 1);
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(null, "Please select menu Not Complete");
            }
        });

        // 4. แก้ไข Task (Edit)
        editButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String currentName = TaskManager.tasks.get(selectedIndex).taskName;
                String newName = JOptionPane.showInputDialog(null, "Edit list name:", currentName);

                // วนถามซ้ำจนกว่าจะมีค่า หรือผู้ใช้กด Cancel
                while (newName != null && newName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please input a new name again!", "Notification",
                            JOptionPane.WARNING_MESSAGE);
                    newName = JOptionPane.showInputDialog(null, "Edit list name:", currentName);
                }

                // newName != null แปลงว่ามีค่าที่ไม่ว่างแล้ว
                if (newName != null) {
                    TaskManager.edit(selectedIndex + 1, newName.trim());
                    refreshTaskList();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select menu to edit");
            }
        });

        // 5. ลบ Task (Delete)
        deleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to delete this list?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    TaskManager.delete(selectedIndex + 1);
                    refreshTaskList();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Please select menu to delete");
            }
        });

        // ดึงข้อมูลมาแสดงครั้งแรก
        refreshTaskList();
    }

    // อัปเดตรายการที่แสดงใน JList ให้ตรงกับ TaskManager.tasks
    private void refreshTaskList() {
        listModel.clear();
        for (int i = 0; i < TaskManager.tasks.size(); i++) {
            Task task = TaskManager.tasks.get(i);
            String statusStr = task.status ? "(Completed)" : "(Not Completed)";
            listModel.addElement((i + 1) + ". " + task.taskName + " " + statusStr);
        }
    }

    public static void main(String[] args) {
        // รันโปรแกรมใน Event Dispatch Thread ของ Swing
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}