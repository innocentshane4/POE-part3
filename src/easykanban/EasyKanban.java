/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package easykanban;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author RC_Student_lab
 */
public class EasyKanban {

    private Login login = new Login();
    private List<Task> tasks = new ArrayList<>();
    private String[] developers;
    private String[] taskNames;
    private String[] taskIDs;
    private int[] taskDurations;
    private String[] taskStatuses;

    public static void main(String[] args) {
        EasyKanban app = new EasyKanban();

        // Display welcome message
        JOptionPane.showMessageDialog(null, "Welcome to EasyKanban");

        boolean registered = false;
        boolean loggedIn = false;

        // Registration process
        do {
            String username = JOptionPane.showInputDialog("Enter username");
            String password = JOptionPane.showInputDialog("Enter password");
            String firstName = JOptionPane.showInputDialog("Enter first name");
            String lastName = JOptionPane.showInputDialog("Enter last name");

            String registrationStatus = app.registerUser(username, password, firstName, lastName);
            JOptionPane.showMessageDialog(null, registrationStatus);
            registered = registrationStatus.equals("User registered successfully");

        } while (!registered);

        // Login process
        do {
            String loginUsername = JOptionPane.showInputDialog("Enter username");
            String loginPassword = JOptionPane.showInputDialog("Enter password");

            boolean loginSuccess = app.loginUser(loginUsername, loginPassword);
            String loginStatus = app.login.returnLoginStatus(loginSuccess);
            JOptionPane.showMessageDialog(null, loginStatus);
            loggedIn = loginSuccess;

        } while (!loggedIn);

        if (loggedIn) {
            // Once logged in, manage tasks
            app.manageTasks();
        }
    }

    // Method to handle task management
    private void manageTasks() {
        // Initialize arrays based on tasks
        initializeArrays();

        // Display menu and handle user choice
        int choice;
        do {
            choice = Integer.parseInt(JOptionPane.showInputDialog(
                    "Select an option:\n" +
                            "1) Add tasks\n" +
                            "2) Show report (Coming Soon)\n" +
                            "3) Update task status\n" +
                            "4) Display tasks with status 'Done'\n" +
                            "5) Display task with longest duration\n" +
                            "6) Search for task by name\n" +
                            "7) Search for tasks by developer\n" +
                            "8) Delete a task by name\n" +
                            "9) Quit"
            ));

            switch (choice) {
                case 1:
                    addTasks();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Feature still in development: Coming Soon");
                    break;
                case 3:
                    updateTaskStatus();
                    break;
                case 4:
                    displayTasksWithStatus("Done");
                    break;
                case 5:
                    displayTaskWithLongestDuration();
                    break;
                case 6:
                    searchTaskByName();
                    break;
                case 7:
                    searchTasksByDeveloper();
                    break;
                case 8:
                    deleteTaskByName();
                    break;
                case 9:
                    JOptionPane.showMessageDialog(null, "Exiting EasyKanban");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please select again.");
                    break;
            }
        } while (choice != 9);
    }

    // Method to initialize arrays based on current tasks
    private void initializeArrays() {
        developers = new String[tasks.size()];
        taskNames = new String[tasks.size()];
        taskIDs = new String[tasks.size()];
        taskDurations = new int[tasks.size()];
        taskStatuses = new String[tasks.size()];

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            developers[i] = task.getDeveloperDetails();
            taskNames[i] = task.getTaskName();
            taskIDs[i] = task.getTaskID();
            taskDurations[i] = task.getTaskDuration();
            taskStatuses[i] = task.getTaskStatus();
        }
    }

    // Method to add tasks
    private void addTasks() {
        int numTasks = Integer.parseInt(JOptionPane.showInputDialog("Enter number of tasks"));
        for (int i = 0; i < numTasks; i++) {
            String taskName = JOptionPane.showInputDialog("Enter task name");
            String taskDescription = JOptionPane.showInputDialog("Enter task description");
            String developerDetails = JOptionPane.showInputDialog("Enter developer details");
            int taskDuration = Integer.parseInt(JOptionPane.showInputDialog("Enter task duration (in hours)"));

            // Validate task description length
            if (!checkTaskDescription(taskDescription)) {
                JOptionPane.showMessageDialog(null, "Task description exceeds 50 characters. Please try again.");
                i--; // Decrement i to retry this task entry
                continue;
            }

            // Create Task object and add to list
            Task task = new Task(taskName, i, taskDescription, developerDetails, taskDuration);
            tasks.add(task);

            JOptionPane.showMessageDialog(null, "Task successfully captured:\n" + task.printTaskDetails());

            // Update arrays
            initializeArrays();
        }
    }

    // Method to update task status
    private void updateTaskStatus() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available to update.");
            return;
        }

        // Display tasks and ask for task number to update
        StringBuilder taskList = new StringBuilder("Tasks:\n");
        for (int i = 0; i < tasks.size(); i++) {
            taskList.append(i + 1).append(": ").append(tasks.get(i).getTaskName()).append("\n");
        }

        int taskIndex = Integer.parseInt(JOptionPane.showInputDialog(taskList + "Enter task number to update status")) - 1;
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            JOptionPane.showMessageDialog(null, "Invalid task number.");
            return;
        }

        String newStatus = JOptionPane.showInputDialog("Enter new status for task: " + tasks.get(taskIndex).getTaskName());
        tasks.get(taskIndex).setTaskStatus(newStatus);
        JOptionPane.showMessageDialog(null, "Task status updated successfully.");

        // Update arrays
        initializeArrays();
    }

    // Method to display tasks with a specific status
    private void displayTasksWithStatus(String status) {
        StringBuilder taskList = new StringBuilder("Tasks with status '" + status + "':\n");
        boolean found = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskStatus().equalsIgnoreCase(status)) {
                taskList.append(tasks.get(i).printTaskDetails()).append("\n");
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "No tasks found with status '" + status + "'.");
        } else {
            JOptionPane.showMessageDialog(null, taskList.toString());
        }
    }

    // Method to display task with longest duration
    private void displayTaskWithLongestDuration() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available.");
            return;
        }

        int longestDuration = 0;
        int longestTaskIndex = 0;

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskDuration() > longestDuration) {
                longestDuration = tasks.get(i).getTaskDuration();
                longestTaskIndex = i;
            }
        }

        JOptionPane.showMessageDialog(null, "Task with longest duration:\n" + tasks.get(longestTaskIndex).printTaskDetails());
    }

    // Method to search for a task by name and display details
    private void searchTaskByName() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available.");
            return;
        }

        String taskNameToSearch = JOptionPane.showInputDialog("Enter task name to search:");

        boolean found = false;
        for (Task task : tasks) {
            if (task.getTaskName().equalsIgnoreCase(taskNameToSearch)) {
                JOptionPane.showMessageDialog(null, "Task details:\n" + task.printTaskDetails());
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Task with name '" + taskNameToSearch + "' not found.");
        }
    }

    // Method to search for tasks assigned to a developer and display details
    private void searchTasksByDeveloper() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available.");
            return;
        }

        String developerToSearch = JOptionPane.showInputDialog("Enter developer name to search tasks:");

        StringBuilder taskList = new StringBuilder("Tasks assigned to '" + developerToSearch + "':\n");
        boolean found = false;

        for (Task task : tasks) {
            if (task.getDeveloperDetails().equalsIgnoreCase(developerToSearch)) {
                taskList.append(task.getTaskName()).append(", Status: ").append(task.getTaskStatus()).append("\n");
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "No tasks found assigned to '" + developerToSearch + "'.");
        } else {
            JOptionPane.showMessageDialog(null, taskList.toString());
        }
    }

    // Method to delete a task by name
    private void deleteTaskByName() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available to delete.");
            return;
        }

        String taskNameToDelete = JOptionPane.showInputDialog("Enter task name to delete:");

        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskName().equalsIgnoreCase(taskNameToDelete)) {
                tasks.remove(i);
                JOptionPane.showMessageDialog(null, "Task '" + taskNameToDelete + "' deleted successfully.");
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Task with name '" + taskNameToDelete + "' not found.");
        }

        // Update arrays
        initializeArrays();
    }

    // Method to check task description length
    private boolean checkTaskDescription(String taskDescription) {
        return taskDescription.length() <= 50;
    }

    // Method to register user
    private String registerUser(String username, String password, String firstName, String lastName) {
        return login.registerUser(username, password, firstName, lastName);
    }

    // Method to login user
    private boolean loginUser(String username, String password) {
        return login.loginUser(username, password);
    }
}

class Login {
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // Method to check if username format is correct
    public boolean checkUserName(String username) {
        return (username.contains("_") && username.length() <= 5);
    }

    // Method to check if password complexity requirements are met
    public boolean checkPasswordComplexity(String password) {
        return (password.length() >= 8 &&
                password.matches(".[A-Z].") &&   // Contains at least one uppercase letter
                password.matches(".[0-9].") &&   // Contains at least one digit
                password.matches(".[^A-Za-z0-9]."));  // Contains at least one special character
    }

    // Method to register user with appropriate messages
    public String registerUser(String username, String password, String firstName, String lastName) {
        if (checkUserName(username) && checkPasswordComplexity(password)) {
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            return "User registered successfully";
        } else {
            return "Username or password is not correctly formatted";
        }
    }

    // Method to login user and return appropriate status message
    public boolean loginUser(String username, String password) {
        return (this.username != null && this.password != null &&
                this.username.equals(username) && this.password.equals(password));
    }

    // Method to return login status message
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again";
        }
    }
}

class Task {
    private String taskName;
    private int taskNumber;
    private String taskDescription;
    private String developerDetails;
    private int taskDuration;
    private String taskID;
    private String taskStatus = "To Do"; // Default task status

    // Constructor to initialize Task object
    public Task(String taskName, int taskNumber, String taskDescription, String developerDetails, int taskDuration) {
        this.taskName = taskName;
        this.taskNumber = taskNumber;
        this.taskDescription = taskDescription;
        this.developerDetails = developerDetails;
        this.taskDuration = taskDuration;
        this.taskID = createTaskID();
    }

    // Method to create task ID
    private String createTaskID() {
        return (taskName.substring(0, 2) + ":" + taskNumber + ":" + developerDetails.substring(developerDetails.length() - 3)).toUpperCase();
    }

    // Method to print full task details
    public String printTaskDetails() {
        return taskStatus + ", " + developerDetails + ", " + taskNumber + ", " + taskName + ", " + taskDescription + ", " + taskID + ", " + taskDuration + " hours";
    }

    // Getter for task duration
    public int getTaskDuration() {
        return taskDuration;
    }

    // Getter for developer details
    public String getDeveloperDetails() {
        return developerDetails;
    }

    // Getter for task name
    public String getTaskName() {
        return taskName;
    }

    // Getter for task ID
    public String getTaskID() {
        return taskID;
    }

    // Getter for task status
    public String getTaskStatus() {
        return taskStatus;
    }

    // Setter for task status
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
