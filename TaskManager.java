import java.util.ArrayList;

public class TaskManager
{
    static  ArrayList<Task> tasks = new ArrayList<>();

    public static void add(String taskName) {
        Task task = new Task(taskName);
        tasks.add(task);
    }

    public static void show() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s %s%n", (i + 1), tasks.get(i).taskName, tasks.get(i).status ? "(Completed)" : "(Not Completed)");
        }
    }

    public static void delete(int number) {
        tasks.remove(number - 1);
    }

    public static void edit(int number, String newName) {
        tasks.get(number - 1).taskName = newName;
    }

    public static void complete(int number) {
        tasks.get(number - 1).status = true;
    }

        public static void notcomplete(int number) {
        tasks.get(number - 1).status = false;
    }

}