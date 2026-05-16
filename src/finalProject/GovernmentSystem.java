package finalProject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

//Main system classes
class Department {
	private String name;        // Department name
	private double budget;      // Budget amount
	private ArrayList<Project> projects = new ArrayList<>();  // Department projects
	private ArrayList<Issue> issues = new ArrayList<>();      // Reported issues
	private int performanceScore; // Performance rating (0-100)
	
private ArrayList<String> budgetHistory = new ArrayList<>();
    
    // Add this method to record budget changes
    public void addBudgetHistory(String entry) {
        budgetHistory.add(new Date() + ": " + entry);
    }
    
    // Add this method to get budget history
    public ArrayList<String> getBudgetHistory() {
        return budgetHistory;
    }
    
	public Department(String name, double budget) {
	    this.name = name;
	    this.budget = budget;
	}
	
	

	// Getter methods to retrieve department details
	public String getName() { return name; }
	public double getBudget() { return budget; }
	public ArrayList<Project> getProjects() { return projects; }
	public ArrayList<Issue> getIssues() { return issues; }
	public int getPerformanceScore() { return performanceScore; }

	// Setter method to update performance score
	public void setPerformanceScore(int score) { performanceScore = score; }
	
	// Method to add a project to the department's list
	public void addProject(Project p) { projects.add(p); }
	
	// Method to add an issue to the department's list
	public void addIssue(Issue i) { issues.add(i); }
	
    // Modify these methods to track changes
    public void addToBudget(double amount) {
        this.budget += amount;
        addBudgetHistory(String.format("Budget increased by %.2f. New budget: %.2f", amount, this.budget));
    }
    
    public void deductFromBudget(double amount) {
        this.budget -= amount;
        addBudgetHistory(String.format("Budget decreased by %.2f. New budget: %.2f", amount, this.budget));
    }

	
}



class Project {
    private String name, status = "Not Started"; // Project name and current status
    private double budget, spent = 0; // Total budget and amount spent
    private int priority; // Priority level of the project

    // Constructor to initialize project details
    public Project(String name, double budget, int priority) {
        this.name = name;
        this.budget = budget;
        this.priority = priority;
    }

    // Add this method to check budget status
    public void checkBudgetStatus(double departmentBudget) {
        if (departmentBudget < this.budget && !status.equals("Lack of Budget")) {
            this.status = "Lack of Budget";
        } else if (departmentBudget >= this.budget && status.equals("Lack of Budget")) {
            this.status = "Not Started";
        }
    }

	// Getter methods to retrieve project details
	public String getName() { return name; }
	public double getBudget() { return budget; }
	public double getSpent() { return spent; }
	public String getStatus() { return status; }
	public int getPriority() { return priority; }

	// Method to update the status of the project
	public void setStatus(String status) { this.status = status; }
	
	// Method to record expenditures
	public void addExpenditure(double amount) { spent += amount; }
}

class Issue {
	private static int counter = 0; // Static counter for generating unique issue IDs
	private String id, description, category, reporter, status = "Pending"; // Issue details
	private int severity; // Severity level of the issue
	private Date lastUpdated = new Date(); // Timestamp of the last update
	
	// Constructor to initialize issue details and generate an ID
	public Issue(String description, String category, int severity, String reporter) {
	   this.id = "ISS" + (++counter);
	   this.description = description;
	   this.category = category;
	   this.severity = severity;
	   this.reporter = reporter;
	}

	// Getter methods to retrieve issue details
	public String getId() { return id; }
	public String getDescription() { return description; }
	public String getCategory() { return category; }
	public int getSeverity() { return severity; }
	public String getReporter() { return reporter; }
	public String getStatus() { return status; }
	public Date getLastUpdated() { return lastUpdated; }
	
	// Method to update the status of the issue
	public void setStatus(String status) { this.status = status; }
	
	// Method to update the last modified date
	public void setLastUpdated(Date d) { lastUpdated = d; }
}

class Citizen {
	private String name, email, district; // Citizen details
	private ArrayList<Issue> issues = new ArrayList<>(); // List of reported issues
	
	// Constructor to initialize citizen details
	public Citizen(String name, String email, String district) {
	   this.name = name;
	   this.email = email;
	   this.district = district;
	}

	// Getter methods to retrieve citizen details
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getDistrict() { return district; }
	
	// Method for citizens to report issues
	public void reportIssue(Issue issue) { issues.add(issue); }
}

//Main GUI Application
public class GovernmentSystem extends JFrame {
  private ArrayList<Department> departments = new ArrayList<>();
  private ArrayList<Citizen> citizens = new ArrayList<>();
  private ArrayList<Issue> allIssues = new ArrayList<>();
  
  private CardLayout cardLayout;
  private JPanel mainPanel;
  
  private final Color PRIMARY_COLOR = new Color(0, 51, 102); // Dark blue
  private final Color ACCENT_COLOR = new Color(255, 153, 0); // Gold accent
  private final Color BACKGROUND_COLOR = new Color(240, 245, 249); // Light blue-gray

  public GovernmentSystem() {
      initializeData();
      setupMainFrame();
      createMainPage();
      createApplicationPages();
  }

  private void initializeData() {
      // Sample departments
      departments.add(new Department("Infrastructure", 0));
      departments.add(new Department("Healthcare", 0));
      departments.add(new Department("Education", 0));
      departments.add(new Department("Public Safety", 0));
      
      // Sample citizens
      citizens.add(new Citizen("Lance Mendoza", "lance@email.com", "District 1"));
      citizens.add(new Citizen("James Bond", "james@email.com", "District 2"));
  }

  private void setupMainFrame() {
      setTitle("Government Management System");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(900, 600);
      setLocationRelativeTo(null);
      
      cardLayout = new CardLayout();
      mainPanel = new JPanel(cardLayout);
      add(mainPanel);
  }

  private void createMainPage() {
      JPanel mainPage = new JPanel(new BorderLayout());
      
      // Background with image
      JPanel backgroundPanel = new JPanel() {
          private Image backgroundImage;

          {
        	  // Load the image with fallback handling
        	  try {
        	      ImageIcon icon = new ImageIcon("D:\\Lance Mendoza\\GMS background.jpg");
        	      if (icon.getImage() != null && icon.getImage() instanceof Image) {
        	          backgroundImage = icon.getImage();
        	      }
        	  } catch (Exception e) {
        	      System.err.println("Warning: Background image not found. Using blank background.");
        	  }
          }
          
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              Graphics2D g2d = (Graphics2D) g;
              g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

              // Draw the image
              g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
          }
      };
      
      backgroundPanel.setLayout(new BorderLayout());
      
      // Title Panel
      JPanel titlePanel = new JPanel();
      titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
      titlePanel.setOpaque(false);
      titlePanel.setBorder(new EmptyBorder(20, 0, 30, 0));
      
      JLabel titleLabel = new JLabel("Government Management System");
      titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      JLabel subtitleLabel = new JLabel("Efficient Governance Through Technology");
      subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 17));
      subtitleLabel.setForeground(Color.LIGHT_GRAY);
      subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      titlePanel.add(titleLabel);
      titlePanel.add(subtitleLabel);
      
      // Menu Panel
      JPanel menuPanel = new JPanel(new GridBagLayout());
      menuPanel.setOpaque(false);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      
      // Menu buttons
      JButton startBtn = createStyledButton("START", ACCENT_COLOR);
      JButton instructionsBtn = createStyledButton("INSTRUCTIONS", new Color(241, 196, 15));
      JButton creditsBtn = createStyledButton("CREDITS", new Color(155, 89, 182));
      JButton exitBtn = createStyledButton("EXIT", new Color(231, 76, 60));
      
      // Button actions
      startBtn.addActionListener(e -> cardLayout.show(mainPanel, "APPLICATION"));
      instructionsBtn.addActionListener(e -> cardLayout.show(mainPanel, "INSTRUCTIONS"));
      creditsBtn.addActionListener(e -> cardLayout.show(mainPanel, "CREDITS"));
      exitBtn.addActionListener(e -> System.exit(0));
      
      gbc.gridx = 0; gbc.gridy = 0;
      menuPanel.add(startBtn, gbc);
      gbc.gridx = 1;
      menuPanel.add(instructionsBtn, gbc);
      gbc.gridx = 0; gbc.gridy = 1;
      menuPanel.add(creditsBtn, gbc);
      gbc.gridx = 1;
      menuPanel.add(exitBtn, gbc);
      
      backgroundPanel.add(titlePanel, BorderLayout.NORTH);
      backgroundPanel.add(menuPanel, BorderLayout.CENTER);
      
      mainPage.add(backgroundPanel);
      mainPanel.add(mainPage, "MAIN");
  }

  private JButton createStyledButton(String text, Color color) {
      JButton button = new JButton(text);
      button.setFont(new Font("Arial", Font.BOLD, 16));
      button.setForeground(Color.WHITE);
      button.setBackground(color);
      button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
      button.setFocusPainted(false);
      button.setCursor(new Cursor(Cursor.HAND_CURSOR));
      button.setPreferredSize(new Dimension(200, 50));
      
      return button;
  }

  private void createApplicationPages() {
      createInstructionsPage();
      createCreditsPage();
      createMainApplicationPage();
  }

  private void createInstructionsPage() {
      JPanel instructionsPage = new JPanel(new BorderLayout());
      instructionsPage.setBackground(BACKGROUND_COLOR);
      
      // Header
      JPanel header = createHeaderPanel("Instructions & How To Use");
      instructionsPage.add(header, BorderLayout.NORTH);
      
      // Content
      JTextArea instructionsText = new JTextArea();
      instructionsText.setEditable(false);
      instructionsText.setFont(new Font("Arial", Font.PLAIN, 17));
      instructionsText.setLineWrap(true);
      instructionsText.setWrapStyleWord(true);
      instructionsText.setBackground(BACKGROUND_COLOR);
      
      String instructions = "Government Management System Instructions\n\n" +
          "Main Features:\n" +
          "- Department Management: Add, view, and manage government departments\n" +
          "- Project Tracking: Create and monitor department projects with budgets\n" +
          "- Citizen Services: Register citizens and handle reported issues\n" +
          "- Issue Management: Track, categorize, and resolve citizen issues\n\n" +
          "How to Use:\n" +
          "1. Department Operations: Use the department panel to manage departments\n" +
          "2. Project Management: Add projects to departments with budgets\n" +
          "3. Citizen Registration: Register new citizens with their information\n" +
          "4. Issue Reporting: Citizens can report issues\n" +
          "5. Status Updates: Update issue status through the workflow\n\n" +
          "Status Definitions:\n" +
          "- Pending: Issue reported but not addressed\n" +
          "- In Progress: Issue being worked on\n" +
          "- Resolved: Issue has been fixed\n" +
          "- Closed: Issue is completed and archived";
      
      instructionsText.setText(instructions);
      
      JScrollPane scrollPane = new JScrollPane(instructionsText);
      instructionsPage.add(scrollPane, BorderLayout.CENTER);
      
      // Back button
      JButton backBtn = createStyledButton("Back to Main", PRIMARY_COLOR);
      backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MAIN"));
      
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setBackground(BACKGROUND_COLOR);
      buttonPanel.add(backBtn);
      instructionsPage.add(buttonPanel, BorderLayout.SOUTH);
      
      mainPanel.add(instructionsPage, "INSTRUCTIONS");
  }

  private void createCreditsPage() {
      JPanel creditsPage = new JPanel(new BorderLayout());
      creditsPage.setBackground(BACKGROUND_COLOR);
      
      // Header
      JPanel header = createHeaderPanel("About the Developer");
      creditsPage.add(header, BorderLayout.NORTH);
      
      // Content
      JTextArea creditsText = new JTextArea();
      creditsText.setEditable(false);
      creditsText.setFont(new Font("Arial", Font.PLAIN, 16));
      creditsText.setLineWrap(true);
      creditsText.setWrapStyleWord(true);
      creditsText.setBackground(BACKGROUND_COLOR);
      
      String credits = "Government Management System\n\n" +
          "Developer Information:\n" +
          "Developer: Lance T. Mendoza\n" +
          "2nd Year in Southern Luzon State University\n"+
          "Bachelor in Computer Engineering\n"+
          "Version: 1.0\n" +
          "Technology: Java Swing GUI\n" +
          "Purpose: Educational Government Management System\n\n" +
          "Project Objectives:\n" +
          "This application demonstrates modern GUI development principles while\n" +
          "providing a comprehensive solution for government management.\n\n" +
          "Key Features Implemented:\n" +
          "- Interactive GUI with modern design\n" +
          "- Comprehensive data management\n" +
          "- User-friendly navigation\n" +
          "- Professional reporting system\n\n" +
          "Thank you for using the Government Management System!";
      
      creditsText.setText(credits);
      creditsText.setAlignmentX(Component.CENTER_ALIGNMENT);
      creditsText.setAlignmentY(Component.CENTER_ALIGNMENT);
      
      JPanel textPanel = new JPanel();
      textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
      textPanel.add(Box.createVerticalGlue());
      textPanel.add(creditsText);
      textPanel.add(Box.createVerticalGlue());
      textPanel.setBackground(BACKGROUND_COLOR);
      
      creditsPage.add(textPanel, BorderLayout.CENTER);
      
      // Back button
      JButton backBtn = createStyledButton("Back to Main", PRIMARY_COLOR);
      backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MAIN"));
      
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setBackground(BACKGROUND_COLOR);
      buttonPanel.add(backBtn);
      creditsPage.add(buttonPanel, BorderLayout.SOUTH);
      
      mainPanel.add(creditsPage, "CREDITS");
  }

private void createMainApplicationPage() {
   JPanel appPage = new JPanel(new BorderLayout());
   appPage.setBackground(BACKGROUND_COLOR);
   
   // Header
   JPanel header = createHeaderPanel("Government Management System");
   appPage.add(header, BorderLayout.NORTH);
   
   // Main content with tabs
   JTabbedPane tabbedPane = new JTabbedPane();
   tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));
   tabbedPane.setBackground(BACKGROUND_COLOR);
   
   // Add tabs first
   tabbedPane.addTab("Departments", createDepartmentPanel());
   tabbedPane.addTab("Projects", createProjectPanel());
   tabbedPane.addTab("Citizens", createCitizenPanel());
   tabbedPane.addTab("Issues", createIssuePanel());
   tabbedPane.addTab("Reports", createReportPanel());
   
   // Set uniform icon size (24x24 pixels recommended)
   final int ICON_SIZE = 24;
   
   // Load and resize icons with null safety (replace paths with your actual icon paths)
   ImageIcon deptIcon = resizeIcon("C:\\Users\\Lance Mendoza\\OneDrive\\Pictures\\Icon Images\\Screenshot 2025-06-04 003415.png", ICON_SIZE, ICON_SIZE);
   ImageIcon projectIcon = resizeIcon("C:\\Users\\Lance Mendoza\\OneDrive\\Pictures\\Icon Images\\Screenshot 2025-06-04 004237.png", ICON_SIZE, ICON_SIZE);
   ImageIcon citizenIcon = resizeIcon("C:\\Users\\Lance Mendoza\\OneDrive\\Pictures\\Icon Images\\Screenshot 2025-06-04 004404.png", ICON_SIZE, ICON_SIZE);
   ImageIcon issueIcon = resizeIcon("C:\\Users\\Lance Mendoza\\OneDrive\\Pictures\\Icon Images\\Screenshot 2025-06-04 004744.png", ICON_SIZE, ICON_SIZE);
   ImageIcon reportIcon = resizeIcon("C:\\Users\\Lance Mendoza\\OneDrive\\Pictures\\Icon Images\\Screenshot 2025-06-04 004502.png", ICON_SIZE, ICON_SIZE);
   
   // Set icons for each tab only if icons loaded successfully
   if (deptIcon != null) tabbedPane.setIconAt(0, deptIcon);
   if (projectIcon != null) tabbedPane.setIconAt(1, projectIcon);
   if (citizenIcon != null) tabbedPane.setIconAt(2, citizenIcon);
   if (issueIcon != null) tabbedPane.setIconAt(3, issueIcon);
   if (reportIcon != null) tabbedPane.setIconAt(4, reportIcon);
   
   // Adjust tab height and padding
   tabbedPane.setPreferredSize(new Dimension(tabbedPane.getWidth(), 40));
   UIManager.put("TabbedPane.tabInsets", new Insets(5, 10, 5, 10));
   
   appPage.add(tabbedPane, BorderLayout.CENTER);
   
   // Footer with navigation
   JPanel footer = new JPanel(new FlowLayout());
   footer.setBackground(PRIMARY_COLOR);
   
   JButton mainMenuBtn = createStyledButton("Main Menu", new Color(52, 73, 94));
   mainMenuBtn.addActionListener(e -> cardLayout.show(mainPanel, "MAIN"));
   footer.add(mainMenuBtn);
   
   appPage.add(footer, BorderLayout.SOUTH);
   
   mainPanel.add(appPage, "APPLICATION");
}

//Add this helper method to your GovernmentSystem class:
private ImageIcon resizeIcon(String path, int width, int height) {
   try {
       // Load the original image
       ImageIcon originalIcon = new ImageIcon(path);
       Image originalImage = originalIcon.getImage();
       
       // Check if image loaded successfully
       if (originalImage == null || originalImage.getWidth(null) <= 0) {
           System.err.println("Warning: Icon not found or invalid: " + path);
           return null;
       }
       
       // Scale it smoothly to the desired size
       Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
       
       // Return the resized icon
       return new ImageIcon(resizedImage);
   } catch (Exception e) {
       System.err.println("Error loading icon: " + path);
       return null;
   }
}

  private JPanel createHeaderPanel(String title) {
      JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
      header.setBackground(PRIMARY_COLOR);
      header.setBorder(new EmptyBorder(15, 0, 15, 0));
      
      JLabel titleLabel = new JLabel(title);
      titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
      titleLabel.setForeground(Color.WHITE);
      header.add(titleLabel);
      
      return header;
  }

  private JPanel createDepartmentPanel() {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBorder(new EmptyBorder(20, 20, 20, 20));
	    panel.setBackground(BACKGROUND_COLOR);
	    
	    // Department list
	    DefaultTableModel model = new DefaultTableModel(new String[]{"Department", "Budget", "Projects", "Issues"}, 0);
	    JTable table = new JTable(model);
	    table.setFont(new Font("Arial", Font.PLAIN, 16));
	    table.setRowHeight(25);
	    
	    updateDepartmentTable(model);
	    
	    JScrollPane scrollPane = new JScrollPane(table);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    
	    // Create a tabbed pane for additional department info
	    JTabbedPane infoPane = new JTabbedPane();
	    
	    // Buttons panel
	    JPanel buttonPanel = new JPanel(new FlowLayout());
	    buttonPanel.setBackground(BACKGROUND_COLOR);
	    
	    JButton addBtn = createStyledButton("Add Department", ACCENT_COLOR);
	    JButton allocateBtn = createStyledButton("Allocate Budget", new Color(46, 204, 113));
	    JButton historyBtn = createStyledButton("View History", new Color(52, 152, 219));
	    JButton refreshBtn = createStyledButton("Refresh", PRIMARY_COLOR);
	    
	    addBtn.addActionListener(e -> addDepartment(model));
	    allocateBtn.addActionListener(e -> allocateBudget(model, table));
	    historyBtn.addActionListener(e -> showBudgetHistory(table));
	    refreshBtn.addActionListener(e -> updateDepartmentTable(model));
	    
	    buttonPanel.add(addBtn);
	    buttonPanel.add(allocateBtn);
	    buttonPanel.add(historyBtn);
	    buttonPanel.add(refreshBtn);
	    
	    panel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    return panel;
	}

  private JPanel createProjectPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBorder(new EmptyBorder(20, 20, 20, 20));
      panel.setBackground(BACKGROUND_COLOR);
      
      // Project list
      DefaultTableModel model = new DefaultTableModel(new String[]{"Project", "Department", "Budget", "Status", "Priority"}, 0);
      JTable table = new JTable(model);
      table.setFont(new Font("Arial", Font.PLAIN, 16));
      table.setRowHeight(25);
      
      updateProjectTable(model);
      
      JScrollPane scrollPane = new JScrollPane(table);
      panel.add(scrollPane, BorderLayout.CENTER);
      
      // Buttons
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setBackground(BACKGROUND_COLOR);
      
      JButton addBtn = createStyledButton("Add Project", ACCENT_COLOR);
      JButton refreshBtn = createStyledButton("Refresh", PRIMARY_COLOR);
      
      addBtn.addActionListener(e -> addProject(model));
      refreshBtn.addActionListener(e -> updateProjectTable(model));
      
      buttonPanel.add(addBtn);
      buttonPanel.add(refreshBtn);
      
      panel.add(buttonPanel, BorderLayout.SOUTH);
      
      return panel;
  }

  private JPanel createCitizenPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBorder(new EmptyBorder(20, 20, 20, 20));
      panel.setBackground(BACKGROUND_COLOR);
      
      // Citizen list
      DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Email", "District", "Issues Reported"}, 0);
      JTable table = new JTable(model);
      table.setFont(new Font("Arial", Font.PLAIN, 16));
      table.setRowHeight(25);
      
      updateCitizenTable(model);
      
      JScrollPane scrollPane = new JScrollPane(table);
      panel.add(scrollPane, BorderLayout.CENTER);
      
      // Buttons
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setBackground(BACKGROUND_COLOR);
      
      JButton addBtn = createStyledButton("Register Citizen", ACCENT_COLOR);
      JButton reportBtn = createStyledButton("Report Issue", new Color(241, 196, 15));
      JButton refreshBtn = createStyledButton("Refresh", PRIMARY_COLOR);
      
      addBtn.addActionListener(e -> registerCitizen(model));
      reportBtn.addActionListener(e -> reportIssue());
      refreshBtn.addActionListener(e -> updateCitizenTable(model));
      
      buttonPanel.add(addBtn);
      buttonPanel.add(reportBtn);
      buttonPanel.add(refreshBtn);
      
      panel.add(buttonPanel, BorderLayout.SOUTH);
      
      return panel;
  }

  private JPanel createIssuePanel() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBorder(new EmptyBorder(20, 20, 20, 20));
      panel.setBackground(BACKGROUND_COLOR);
      
      
      
      // Issue list
      DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Description", "Department", "Severity", "Reporter", "Status"}, 0);
      JTable table = new JTable(model);
      table.setFont(new Font("Arial", Font.PLAIN, 16));
      table.setRowHeight(25);
      
      updateIssueTable(model);
      
      JScrollPane scrollPane = new JScrollPane(table);
      panel.add(scrollPane, BorderLayout.CENTER);
      
      // Buttons
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setBackground(BACKGROUND_COLOR);
      
      JButton updateBtn = createStyledButton("Update Status", new Color(241, 196, 15));
      JButton refreshBtn = createStyledButton("Refresh", PRIMARY_COLOR);
      
      updateBtn.addActionListener(e -> updateIssueStatus(model));
      refreshBtn.addActionListener(e -> updateIssueTable(model));
      
      buttonPanel.add(updateBtn);
      buttonPanel.add(refreshBtn);
      
      panel.add(buttonPanel, BorderLayout.SOUTH);
      
      return panel;
  }

  private JPanel createReportPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBorder(new EmptyBorder(20, 20, 20, 20));
      panel.setBackground(BACKGROUND_COLOR);
      
      JTextArea reportArea = new JTextArea();
      reportArea.setEditable(false);
      reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
      reportArea.setBackground(Color.WHITE);
      
      generateReport(reportArea);
      
      JScrollPane scrollPane = new JScrollPane(reportArea);
      panel.add(scrollPane, BorderLayout.CENTER);
      
      // Refresh button
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setBackground(BACKGROUND_COLOR);
      
      JButton refreshBtn = createStyledButton("Generate Report", PRIMARY_COLOR);
      refreshBtn.addActionListener(e -> generateReport(reportArea));
      
      buttonPanel.add(refreshBtn);
      panel.add(buttonPanel, BorderLayout.SOUTH);
      
      return panel;
  }
  
  // Helper methods for data management
  private void updateDepartmentTable(DefaultTableModel model) {
      model.setRowCount(0);
      for (Department dept : departments) {
          model.addRow(new Object[]{
              dept.getName(),
              String.format("%.2f", dept.getBudget()),
              dept.getProjects().size(),
              dept.getIssues().size()
          });
      }
  }

  private void updateProjectTable(DefaultTableModel model) {
      model.setRowCount(0);
      for (Department dept : departments) {
          for (Project project : dept.getProjects()) {
              model.addRow(new Object[]{
                  project.getName(),
                  dept.getName(),
                  String.format("%.2f", project.getBudget()),
                  project.getStatus(),
                  project.getPriority()
              });
          }
      }
  }

  private void updateCitizenTable(DefaultTableModel model) {
      model.setRowCount(0);
      for (Citizen citizen : citizens) {
          model.addRow(new Object[]{
              citizen.getName(),
              citizen.getEmail(),
              citizen.getDistrict(),
              (int) allIssues.stream().filter(issue -> issue.getReporter().equals(citizen.getName())).count()
          });
      }
  }

  private void updateIssueTable(DefaultTableModel model) {
      model.setRowCount(0);
      // Sort by severity (highest first)
      List<Issue> sortedIssues = new ArrayList<>(allIssues);
      sortedIssues.sort((a, b) -> Integer.compare(b.getSeverity(), a.getSeverity()));
      
      for (Issue issue : sortedIssues) {
          String description = issue.getDescription();
          if (description.length() > 30) {
              description = description.substring(0, 27) + "...";
          }
          model.addRow(new Object[]{
              issue.getId(),
              description,
              issue.getCategory(),
              issue.getSeverity(),
              issue.getReporter(),
              issue.getStatus()
          });
      }
  }

  private void addDepartment(DefaultTableModel model) {
	    JTextField nameField = new JTextField();
	    JTextField budgetField = new JTextField();

	    JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
	    panel.add(new JLabel("Department Name:"));
	    panel.add(nameField);
	    panel.add(new JLabel("Budget:"));
	    panel.add(budgetField);

	    int result = JOptionPane.showConfirmDialog(this, panel, "Add Department", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	        String name = nameField.getText().trim();
	        Optional<Double> budgetOpt = parseBudget(budgetField.getText().trim());

	        if (!name.isEmpty() && budgetOpt.isPresent() && budgetOpt.get() > 0) {
	            departments.add(new Department(name, budgetOpt.get()));
	            updateDepartmentTable(model);
	            JOptionPane.showMessageDialog(this, "Department added successfully!");
	        } else {
	            JOptionPane.showMessageDialog(this, "Please enter valid data!");
	        }
	    }
	}

  private Optional<Double> parseBudget(String budgetStr) {
	    if (budgetStr.matches("\\d+(\\.\\d+)?")) { // Ensures the input is a valid number
	        return Optional.of(Double.parseDouble(budgetStr));
	    }
	    JOptionPane.showMessageDialog(null, "Please enter a valid budget amount!");
	    return Optional.empty();
	}
  
  private void allocateBudget(DefaultTableModel model, JTable table) {
	    int selectedRow = table.getSelectedRow();
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, "Please select a department first!");
	        return;
	    }
	    
	    String deptName = (String) table.getValueAt(selectedRow, 0);
	    Department department = departments.stream()
	        .filter(d -> d.getName().equals(deptName))
	        .findFirst()
	        .orElse(null);
	    
	    if (department == null) {
	        JOptionPane.showMessageDialog(this, "Department not found!");
	        return;
	    }
	    
	    JTextField amountField = new JTextField();
	    JTextArea reasonField = new JTextArea(3, 20);
	    
	    JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
	    panel.add(new JLabel("Allocation Amount:"));
	    panel.add(amountField);
	    panel.add(new JLabel("Reason/Purpose:"));
	    panel.add(new JScrollPane(reasonField));
	    
	    int result = JOptionPane.showConfirmDialog(this, panel, 
	        "Allocate Budget for " + deptName, 
	        JOptionPane.OK_CANCEL_OPTION);
	    
	    if (result == JOptionPane.OK_OPTION) {
	        try {
	            double amount = Double.parseDouble(amountField.getText().trim());
	            String reason = reasonField.getText().trim();
	            
	            if (amount <= 0) {
	                JOptionPane.showMessageDialog(this, "Amount must be positive!");
	                return;
	            }
	            
	            department.addToBudget(amount);
	            department.addBudgetHistory(String.format("Allocated %.2f for: %s", amount, 
	                reason.isEmpty() ? "No reason provided" : reason));
	            
	            // Check if any pending projects can now be funded
	            for (Project project : department.getProjects()) {
	                if (project.getStatus().equals("Lack of Budget") && 
	                    department.getBudget() >= project.getBudget()) {
	                    department.deductFromBudget(project.getBudget());
	                    project.setStatus("Not Started");
	                    department.addBudgetHistory(String.format(
	                        "Project '%s' activated with budget %.2f", 
	                        project.getName(), project.getBudget()));
	                }
	            }
	            
	            updateDepartmentTable(model);
	            updateProjectTable(model);
	            JOptionPane.showMessageDialog(this, "Budget allocated successfully!");
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(this, "Please enter a valid amount!");
	        }
	    }
	}

	private void showBudgetHistory(JTable table) {
	    int selectedRow = table.getSelectedRow();
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, "Please select a department first!");
	        return;
	    }
	    
	    String deptName = (String) table.getValueAt(selectedRow, 0);
	    Department department = departments.stream()
	        .filter(d -> d.getName().equals(deptName))
	        .findFirst()
	        .orElse(null);
	    
	    if (department == null) {
	        JOptionPane.showMessageDialog(this, "Department not found!");
	        return;
	    }
	    
	    ArrayList<String> history = department.getBudgetHistory();
	    if (history.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "No budget history available for this department.");
	        return;
	    }
	    
	    JTextArea historyArea = new JTextArea(15, 40);
	    historyArea.setEditable(false);
	    historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
	    
	    StringBuilder historyText = new StringBuilder();
	    historyText.append("Budget History for ").append(deptName).append("\n\n");
	    historyText.append("Current Budget: ").append(String.format("%.2f", department.getBudget())).append("\n\n");
	    
	    for (String entry : history) {
	        historyText.append(entry).append("\n");
	    }
	    
	    historyArea.setText(historyText.toString());
	    JOptionPane.showMessageDialog(this, new JScrollPane(historyArea), 
	        "Budget History - " + deptName, JOptionPane.PLAIN_MESSAGE);
	}

	private void addProject(DefaultTableModel model) {
	    if (departments.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please add departments first!");
	        return;
	    }

	    JTextField nameField = new JTextField();
	    JTextField budgetField = new JTextField();
	    JTextField priorityField = new JTextField();
	    JComboBox<String> deptCombo = new JComboBox<>();

	    for (Department dept : departments) {
	        deptCombo.addItem(dept.getName());
	    }

	    JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
	    panel.add(new JLabel("Project Name:"));
	    panel.add(nameField);
	    panel.add(new JLabel("Department:"));
	    panel.add(deptCombo);
	    panel.add(new JLabel("Budget:"));
	    panel.add(budgetField);
	    panel.add(new JLabel("Priority (1-10):"));
	    panel.add(priorityField);

	    int result = JOptionPane.showConfirmDialog(this, panel, "Add Project", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	        String name = nameField.getText().trim();
	        Double budget = parseDouble(budgetField.getText().trim(), "budget");
	        Integer priority = parseInteger(priorityField.getText().trim(), "priority");

	        if (!name.isEmpty() && budget != null && budget > 0 && priority != null && priority >= 1 && priority <= 10) {
	            Department selectedDept = departments.get(deptCombo.getSelectedIndex());
	            Project newProject = new Project(name, budget, priority);
	            
	            // Check budget status
	            newProject.checkBudgetStatus(selectedDept.getBudget());
	            
	            selectedDept.addProject(newProject);
	            
	            // Only deduct from budget if department has enough funds
	            if (selectedDept.getBudget() >= budget) {
	                selectedDept.deductFromBudget(budget);
	                selectedDept.addBudgetHistory(String.format(
	                    "Project '%s' created with budget %.2f (Priority %d)", 
	                    name, budget, priority));
	            } else {
	                selectedDept.addBudgetHistory(String.format(
	                    "Project '%s' created with budget %.2f (Priority %d) - Insufficient funds (Pending)",
	                    name, budget, priority));
	            }
	            
	            updateProjectTable(model);
	            updateDepartmentTable(model);
	            JOptionPane.showMessageDialog(this, "Project added successfully!");
	        } else {
	            JOptionPane.showMessageDialog(this, "Please enter valid data!");
	        }
	    }
	}

	private Double parseDouble(String input, String fieldName) {
	    if (input.matches("\\d+(\\.\\d+)?")) {
	        return Double.parseDouble(input);
	    }
	    JOptionPane.showMessageDialog(null, "Please enter a valid " + fieldName + " value!");
	    return null;
	}

	private Integer parseInteger(String input, String fieldName) {
	    if (input.matches("\\d+")) {
	        return Integer.parseInt(input);
	    }
	    JOptionPane.showMessageDialog(null, "Please enter a valid " + fieldName + " value!");
	    return null;
	}

	private void registerCitizen(DefaultTableModel model) {
		JTextField nameField = new JTextField();
		JTextField emailField = new JTextField();
		JTextField districtField = new JTextField();
      
		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
		panel.add(new JLabel("Name:"));
		panel.add(nameField);
		panel.add(new JLabel("Email:"));
		panel.add(emailField);
		panel.add(new JLabel("District:"));
		panel.add(districtField);
      
		int result = JOptionPane.showConfirmDialog(this, panel, "Register Citizen", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String name = nameField.getText().trim();
			String email = emailField.getText().trim();
			String district = districtField.getText().trim();
          
			if (!name.isEmpty() && !email.isEmpty() && !district.isEmpty()) {
				citizens.add(new Citizen(name, email, district));
				updateCitizenTable(model);
				JOptionPane.showMessageDialog(this, "Citizen registered successfully!");
			} else {
				JOptionPane.showMessageDialog(this, "Please fill all fields!");
			}
		}
	}

	private void reportIssue() {
	    if (citizens.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please register citizens first!");
	        return;
	    }

	    if (departments.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "No departments available to assign issues to!");
	        return;
	    }

	    JTextArea descField = new JTextArea(3, 30);
	    JComboBox<String> deptCombo = new JComboBox<>();
	    JComboBox<Integer> severityCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
	    JComboBox<String> citizenCombo = new JComboBox<>();

	    // Populate department combo
	    for (Department dept : departments) {
	        deptCombo.addItem(dept.getName());
	    }

	    // Populate citizen combo
	    for (Citizen citizen : citizens) {
	        citizenCombo.addItem(citizen.getName());
	    }

	    // Set default severity to 5 (medium)
	    severityCombo.setSelectedItem(5);

	    JPanel panel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.anchor = GridBagConstraints.WEST;

	    gbc.gridx = 0; gbc.gridy = 0;
	    panel.add(new JLabel("Reporter:"), gbc);
	    gbc.gridx = 1;
	    panel.add(citizenCombo, gbc);

	    gbc.gridx = 0; gbc.gridy = 1;
	    panel.add(new JLabel("Department:"), gbc);
	    gbc.gridx = 1;
	    panel.add(deptCombo, gbc);

	    gbc.gridx = 0; gbc.gridy = 2;
	    panel.add(new JLabel("Description:"), gbc);
	    gbc.gridx = 1;
	    panel.add(new JScrollPane(descField), gbc);

	    gbc.gridx = 0; gbc.gridy = 3;
	    panel.add(new JLabel("Severity (1-10):"), gbc);
	    gbc.gridx = 1;
	    panel.add(severityCombo, gbc);

	    int result = JOptionPane.showConfirmDialog(this, panel, "Report Issue", 
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

	    if (result == JOptionPane.OK_OPTION) {
	        String description = descField.getText().trim();
	        String department = (String) deptCombo.getSelectedItem();
	        int severity = (int) severityCombo.getSelectedItem();
	        String reporter = (String) citizenCombo.getSelectedItem();

	        if (!description.isEmpty()) {
	            // Find the selected department
	            Department selectedDept = departments.stream()
	                .filter(d -> d.getName().equals(department))
	                .findFirst()
	                .orElse(null);

	            if (selectedDept != null) {
	                Issue issue = new Issue(description, department, severity, reporter);
	                selectedDept.addIssue(issue);
	                allIssues.add(issue);
	                
	                // Find and update the citizen
	                citizens.stream()
	                    .filter(c -> c.getName().equals(reporter))
	                    .findFirst()
	                    .ifPresent(c -> c.reportIssue(issue));
	                
	                JOptionPane.showMessageDialog(this, 
	                    "Issue reported successfully!\n" +
	                    "Issue ID: " + issue.getId() + "\n" +
	                    "Assigned to: " + selectedDept.getName());
	            } else {
	                JOptionPane.showMessageDialog(this, "Error: Department not found!");
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Please enter a description!");
	        }
	    }
	}
	
	private void updateIssueStatus(DefaultTableModel model) {
		if (allIssues.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No issues to update!");
			return;
		}
      
		String[] issueOptions = new String[allIssues.size()];
		for (int i = 0; i < allIssues.size(); i++) {
			Issue issue = allIssues.get(i);
			String descriptionShort = issue.getDescription();
			if (descriptionShort.length() > 30) {
				descriptionShort = descriptionShort.substring(0, 27) + "...";
			}
			issueOptions[i] = issue.getId() + " - " + descriptionShort;
		}
      
			String selectedIssue = (String) JOptionPane.showInputDialog(this, 
					"Select issue to update:", "Update Issue Status", 
					JOptionPane.QUESTION_MESSAGE, null, issueOptions, issueOptions[0]);
      
			if (selectedIssue != null) {
				int issueIndex = Arrays.asList(issueOptions).indexOf(selectedIssue);
				Issue issue = allIssues.get(issueIndex);
          
				String[] statusOptions = {"Pending", "In Progress", "Resolved", "Closed"};
				String newStatus = (String) JOptionPane.showInputDialog(this,
						"Current Status: " + issue.getStatus() + "\n\nSelect new status:",
						"Update Status", JOptionPane.QUESTION_MESSAGE, null, statusOptions, issue.getStatus());
          
				if (newStatus != null && !newStatus.equals(issue.getStatus())) {
					issue.setStatus(newStatus);
					issue.setLastUpdated(new Date());
					updateIssueTable(model);
					JOptionPane.showMessageDialog(this, "Status updated successfully!");
				}
			}
	}

	private void generateReport(JTextArea reportArea) {
		StringBuilder report = new StringBuilder();
      
		report.append("GOVERNMENT MANAGEMENT SYSTEM REPORT\n");
		report.append("===================================\n");
		report.append(String.format("Generated: %s\n\n", new Date().toString()));

		// Department Summary
		report.append("DEPARTMENT OVERVIEW\n");
		report.append("------------------\n");
		report.append(String.format("%-20s %-15s %-10s %-10s\n", "Department", "Budget", "Projects", "Issues"));
      
		double totalBudget = 0;
		int totalProjects = 0;
		int totalIssues = 0;
      
		for (Department dept : departments) {
			report.append(String.format("%-20s %-14.2f %-10d %-10d\n", 
					dept.getName(), dept.getBudget(), dept.getProjects().size(), dept.getIssues().size()));
			totalBudget += dept.getBudget();
			totalProjects += dept.getProjects().size();
			totalIssues += dept.getIssues().size();
		}
      
		report.append(String.format("%-20s %-14.2f %-10d %-10d\n\n", 
				"TOTAL", totalBudget, totalProjects, totalIssues));

		// Issue Status Summary
		report.append("ISSUE STATUS SUMMARY\n");
		report.append("--------------------\n");
      
		long pending = allIssues.stream().filter(i -> i.getStatus().equals("Pending")).count();
		long inProgress = allIssues.stream().filter(i -> i.getStatus().equals("In Progress")).count();
		long resolved = allIssues.stream().filter(i -> i.getStatus().equals("Resolved")).count();
		long closed = allIssues.stream().filter(i -> i.getStatus().equals("Closed")).count();
      
		report.append(String.format("Pending:     %d issues\n", pending));
		report.append(String.format("In Progress: %d issues\n", inProgress));
		report.append(String.format("Resolved:    %d issues\n", resolved));
		report.append(String.format("Closed:      %d issues\n\n", closed));

		// Citizen Information
		report.append("CITIZEN SUMMARY\n");
		report.append("---------------\n");
		report.append(String.format("Total Registered Citizens: %d\n", citizens.size()));
		report.append(String.format("Total Issues Reported:     %d\n\n", allIssues.size()));

		// High Priority Issues
		if (!allIssues.isEmpty()) {
			report.append("HIGH SEVERITY ISSUES (Severity 8-10)\n");
			report.append("------------------------------------\n");
          
			List<Issue> highPriorityIssues = allIssues.stream()
					.filter(issue -> issue.getSeverity() >= 8)
					.sorted((a, b) -> Integer.compare(b.getSeverity(), a.getSeverity()))
					.collect(Collectors.toList());
          
			if (highPriorityIssues.isEmpty()) {
				report.append("No high severity issues found.\n\n");
			} else {
				report.append(String.format("%-12s %-25s %-10s %-15s\n", "ID", "Description", "Severity", "Status"));
              
				for (Issue issue : highPriorityIssues) {
					String desc = issue.getDescription();
					if (desc.length() > 23) {
						desc = desc.substring(0, 20) + "...";
					}
					report.append(String.format("%-12s %-25s %-10d %-15s\n",
						issue.getId(), desc, issue.getSeverity(), issue.getStatus()));
				}
				report.append("\n");
			}
		}

		// Department Performance
		report.append("DEPARTMENT PERFORMANCE\n");
		report.append("----------------------\n");
      
		for (Department dept : departments) {
			long deptResolved = dept.getIssues().stream().filter(i -> i.getStatus().equals("Resolved") || i.getStatus().equals("Closed")).count();
			long deptTotal = dept.getIssues().size();
			double resolutionRate = deptTotal > 0 ? (double) deptResolved / deptTotal * 100 : 0;
          
			report.append(String.format("%-20s: %d total issues, %.1f%% resolved\n", 
					dept.getName(), deptTotal, resolutionRate));
		}
      
		String separator = new String(new char[40]).replace('\0', '=');
		report.append("\n" + separator + "\n");
		report.append("Report End - Government Management System v1.0\n");
      
		reportArea.setText(report.toString());
		reportArea.setCaretPosition(0);
	} 
}