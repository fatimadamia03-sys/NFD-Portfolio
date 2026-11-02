import os
import sys

# Import from models
from models import Comment, Task, User, Student, GroupLeader, Lecturer, Team, Meeting, Report
from utils.helpers import clear_screen, print_header, print_section, validate_date, validate_email

class CollaborationPlatform:
    def __init__(self):
        self.current_user = None
        self.teams = []
        self.all_users = []
        self.demo_mode = True
        self.setup_demo_data()
    
    def setup_demo_data(self):
        # Create demo users
        self.student1 = Student("S001", "Ali", "ali@student.com", "pass1", "M701", "CS101")
        self.student2 = Student("S002", "Abu", "abu@student.com", "pass2", "M702", "CS101")
        self.student3 = Student("S003", "Chong", "chong@student.com", "pass3", "M703", "CS101")
        self.leader = GroupLeader("GL01", "Risa", "risa@leader.com", "risa123", "M700", "CS101")
        self.lecturer = Lecturer("L001", "Dr. Sarah", "sarah@edu.my", "staff1", "ST99", "Computing")
        
        self.all_users.extend([self.student1, self.student2, self.student3, self.leader, self.lecturer])
        
        # Create demo team
        team = self.leader.create_team("Alpha Team", "Final Year Project: Smart Campus")
        
        # Create demo tasks
        task1 = Task("T001", "Design Database", "2025-12-30", "High")
        task2 = Task("T002", "Implement UI", "2025-12-28", "High")
        task3 = Task("T003", "Write Report", "2026-01-05", "Medium")
        task4 = Task("T004", "Test System", "2026-01-10", "Medium")
        
        # Assign tasks to DIFFERENT members
        self.leader.assign_task(task1, self.student1)
        self.leader.assign_task(task2, self.student2)
        self.leader.assign_task(task3, self.student3)
        self.leader.assign_task(task4, self.leader)
        
        # Add tasks to team
        team.tasks.extend([task1, task2, task3, task4])
        
        # Add all demo students to the team
        for student in [self.student1, self.student2, self.student3]:
            team.add_member(student)
            student.team = team
        
        self.teams.append(team)
        
        print("\nDemo data created successfully!")
        print("  Team: Alpha Team (4 members, 4 tasks)")
        print("  Members: Risa (Leader), Ali, Abu, Chong")
        print("  Lecturer: Dr. Sarah")
        input("\nPress Enter to continue...")
    
    def show_welcome(self):
        print_header("SMART STUDENT TEAM COLLABORATION PLATFORM")
        print("Object-Oriented Software Development Project".center(60))

    def main_menu(self):
        while True:
            clear_screen()
            self.show_welcome()
            
            print("\n" + "═"*40)
            print("MAIN MENU")
            print("═"*40)
            print("1. Login as Group Leader")
            print("2. Login as Student")
            print("3. Login as Lecturer")
            print("4. View System Information")
            print("5. View All Students")
            print("0. Exit")
            print("═"*40)
            
            choice = input("\nEnter your choice: ").strip()
            
            if choice == "1":
                self.login_user(self.leader)
            elif choice == "2":
                print("\nSelect Student:")
                print("1. Ali")
                print("2. Abu")
                student_choice = input("Choice: ").strip()
                if student_choice == "1":
                    self.login_user(self.student1)
                elif student_choice == "2":
                    self.login_user(self.student2)
                else:
                    print("Invalid choice")
                    input("\nPress Enter to continue...")
            elif choice == "3":
                self.login_user(self.lecturer)
            elif choice == "4":
                self.view_system_info()
            elif choice == "5":
                self.view_all_students()
                input("\nPress Enter to continue...")
            elif choice == "0":
                print("\nThank you for using the system. Goodbye!")
                break
            else:
                print("Invalid choice. Please try again.")
                input("\nPress Enter to continue...")
    
    def login_user(self, user):
        clear_screen()
        print("\n" + "="*50)
        print(f"LOGIN: {user.name}".center(50))
        print("="*50)
        
        # For demo, suggest but don't auto-fill
        if self.demo_mode:
            user_id = input(f"\nUser ID (demo: {user.user_id}): ").strip()
            password = input(f"Password (demo: {user.password}): ").strip()
        else:
            user_id = input("User ID: ").strip()
            password = input("Password: ").strip()
        
        # Don't accept empty inputs
        if user_id == "" or password == "":
            print("\nError: User ID and Password cannot be empty!")
            input("\nPress Enter to continue...")
            return
        
        if user.login(user_id, password):
            self.current_user = user
            print(f"\nWelcome {user.name}!")
            input("\nPress Enter to continue to your dashboard...")
            self.user_dashboard()
        else:
            print("\nLogin failed. Please check your credentials.")
            input("\nPress Enter to continue...")
    
    def user_dashboard(self):
        while self.current_user:
            clear_screen()
            print("\n" + "="*50)
            print(f"DASHBOARD: {self.current_user.name}".center(50))
            print("="*50)
            
            # Show role-specific menu
            self.current_user.display_menu()
            
            try:
                choice = input("\nSelect option: ").strip()
                
                if isinstance(self.current_user, Student) and not isinstance(self.current_user, GroupLeader):
                    self.handle_student_menu(choice)
                elif isinstance(self.current_user, GroupLeader):
                    self.handle_leader_menu(choice)
                elif isinstance(self.current_user, Lecturer):
                    self.handle_lecturer_menu(choice)
                elif choice == "0":
                    self.current_user.logout()
                    self.current_user = None
                    print("\nLogged out successfully.")
                    input("\nPress Enter to continue...")
                    break
                else:
                    print("Invalid option. Please try again.")
                    input("\nPress Enter to continue...")
                    
            except Exception as e:
                print(f"Error: {e}")
                input("\nPress Enter to continue...")
    
    def handle_student_menu(self, choice):
        if choice == "1":
            self.view_student_tasks()
        elif choice == "2":
            self.update_task_status()
        elif choice == "3":
            self.view_team_info()
        elif choice == "4":
            self.add_time_to_task()
        elif choice == "5":
            self.add_file_to_task()
        elif choice == "6":
            self.view_profile()
        elif choice == "0":
            self.current_user.logout()
            self.current_user = None
            return
        else:
            print("Invalid option")
        
        input("\nPress Enter to continue...")
    
    def handle_leader_menu(self, choice):
        if choice == "1":
            self.create_new_team()
        elif choice == "2":
            self.assign_new_task()
        elif choice == "3":
            self.view_team_progress()
        elif choice == "4":
            self.schedule_meeting()
        elif choice == "5":
            self.invite_members()
        elif choice == "6":
            self.view_all_tasks()
        elif choice == "7":
            self.remove_member()
        elif choice == "0":
            self.current_user.logout()
            self.current_user = None
            return
        else:
            print("Invalid option")
        
        input("\nPress Enter to continue...")
    
    def handle_lecturer_menu(self, choice):
        if choice == "1":
            self.view_team_summaries()
        elif choice == "2":
            self.provide_feedback()
        elif choice == "3":
            self.view_student_progress()
        elif choice == "4":
            self.generate_reports()
        elif choice == "0":
            self.current_user.logout()
            self.current_user = None
            return
        else:
            print("Invalid option")
        
        input("\nPress Enter to continue...")

    # ================= STUDENT FUNCTIONS =================
    def view_student_tasks(self):
        print(f"\nYOUR TASKS")
        print("─" * 40)
        if not self.current_user.tasks:
            print("No tasks assigned.")
        for task in self.current_user.tasks:
            print(f"• {task.title}")
            print(f"  Status: {task.status} | Priority: {task.priority}")
            print(f"  Deadline: {task.deadline}")
            print()
    
    def update_task_status(self):
        self.view_student_tasks()
        if not self.current_user.tasks:
            print("No tasks to update")
            return
        
        task_id = input("\nEnter Task ID to update: ").strip()
        
        # Validate input
        if not task_id:
            print("Task ID cannot be empty!")
            return
        
        # Check if task exists
        task_exists = any(task.task_id == task_id for task in self.current_user.tasks)
        if not task_exists:
            print(f"Task with ID '{task_id}' not found in your tasks")
            return
        
        print("\nStatus options:")
        print("1. To Do")
        print("2. In Progress")
        print("3. Completed")
        
        status_choice = input("Select status (1-3): ").strip()
        status_map = {"1": "To Do", "2": "In Progress", "3": "Completed"}
        
        if status_choice in status_map:
            if self.current_user.update_task_status(task_id, status_map[status_choice]):
                print("Task status updated")
        else:
            print("Invalid status choice")
    
    def view_team_info(self):
        if self.current_user.team:
            print(self.current_user.team.get_team_details())
        else:
            print("You are not in any team.")
    
    def add_time_to_task(self):
        self.view_student_tasks()
        if not self.current_user.tasks:
            print("No tasks assigned")
            return
        
        task_id = input("\nEnter Task ID: ").strip()
        
        # Validate input
        if not task_id:
            print("Task ID cannot be empty!")
            return
        
        # Check if task exists
        task_exists = any(task.task_id == task_id for task in self.current_user.tasks)
        if not task_exists:
            print(f"Task with ID '{task_id}' not found in your tasks")
            return
        
        time_spent = input("Enter time spent (e.g., '2 hours'): ").strip()
        
        # Validate time input
        if not time_spent:
            print("Time spent cannot be empty!")
            return
        
        self.current_user.add_time_to_task(task_id, time_spent)
    
    def add_file_to_task(self):
        self.view_student_tasks()
        if not self.current_user.tasks:
            print("No tasks assigned")
            return
        
        task_id = input("\nEnter Task ID: ").strip()
        
        # Validate input
        if not task_id:
            print("Task ID cannot be empty!")
            return
        
        # Check if task exists
        task_exists = any(task.task_id == task_id for task in self.current_user.tasks)
        if not task_exists:
            print(f"Task with ID '{task_id}' not found in your tasks")
            return
        
        filename = input("Enter filename: ").strip()
        
        # Validate filename
        if not filename:
            print("Filename cannot be empty!")
            return
        
        self.current_user.add_file_to_task(task_id, filename)
    
    def view_profile(self):
        print(f"\nPROFILE INFORMATION")
        print("─" * 30)
        print(f"Name: {self.current_user.name}")
        print(f"Email: {self.current_user.email}")
        print(f"Matric No: {self.current_user.matric_no}")
        print(f"Course: {self.current_user.course}")
        print(f"Team: {self.current_user.team.team_name if self.current_user.team else 'None'}")
    
    # ================= LEADER FUNCTIONS =================
    def create_new_team(self):
        print("\nCREATE NEW TEAM")
        name = input("Team name: ").strip()
        details = input("Project details: ").strip()
        
        new_team = self.current_user.create_team(name, details)
        if new_team:
            self.teams.append(new_team)
            print("New team created successfully!")
    
    def assign_new_task(self):
        """Assign a new task to any team member"""
        if not self.current_user.team:
            print("\nYou need to create or join a team first!")
            return

        if len(self.current_user.team.members) == 0:
            print("\nNo members in the team. Add members first!")
            return

        print("\n" + "="*50)
        print("ASSIGN NEW TASK")
        print("="*50)

        # Get task details with validation
        while True:
            task_id = input("Task ID (e.g., TASK-001): ").strip()
            if not task_id:
                print("Task ID cannot be empty!")
                continue
            
            # Check if task ID already exists in team
            task_exists = any(task.task_id == task_id for task in self.current_user.team.tasks)
            if task_exists:
                print(f"Task ID '{task_id}' already exists. Please use a different ID.")
                continue
                
            break

        while True:
            title = input("Task title: ").strip()
            if not title:
                print("Task title is required!")
                continue
            break

        while True:
            deadline = input("Deadline (YYYY-MM-DD): ").strip()
            if not deadline:
                print("Deadline is required!")
                continue
            # Simple date format validation
            if len(deadline) != 10 or deadline[4] != '-' or deadline[7] != '-':
                print("Invalid date format. Please use YYYY-MM-DD")
                continue
            break

        priority = input("Priority (High/Medium/Low) [Medium]: ").strip() or "Medium"
        
        # Validate priority
        priority = priority.capitalize()
        if priority not in ["High", "Medium", "Low"]:
            priority = "Medium"

        try:
            # Create the task with validation
            task = Task(task_id, title, deadline, priority)
        except ValueError as e:
            print(f"Error creating task: {e}")
            return

        # Show ALL team members
        print(f"\nTEAM MEMBERS ({len(self.current_user.team.members)}):")
        print("-" * 30)
        for i, member in enumerate(self.current_user.team.members, 1):
            # Count how many tasks each member already has
            member_task_count = len([t for t in self.current_user.team.tasks if t.assigned_to == member])
            role = " (Leader)" if member == self.current_user else ""
            print(f"{i}. {member.name}{role} - {member_task_count} task(s)")
        
        try:
            choice = int(input(f"\nSelect member to assign (1-{len(self.current_user.team.members)}): ").strip()) - 1
            
            if 0 <= choice < len(self.current_user.team.members):
                selected_member = self.current_user.team.members[choice]
            
                # Assign the task
                task.assigned_to = selected_member
                selected_member.tasks.append(task)
                self.current_user.team.tasks.append(task)
            
                print(f"\nSUCCESS!")
                print(f"Task '{title}' assigned to {selected_member.name}")
                print(f"Task ID: {task_id}")
                print(f"Deadline: {deadline}")
                print(f"Priority: {priority}")
            else:
                print(f"Please select a number between 1 and {len(self.current_user.team.members)}")
            
        except ValueError:
            print("Please enter a valid number")
        except Exception as e:
            print(f"Error: {e}")
    
    def view_team_progress(self):
        if self.current_user.team:
            print(f"\nTEAM PROGRESS")
            print(f"Team: {self.current_user.team.team_name}")
            print(f"Overall Progress: {self.current_user.team.get_overall_progress():.1f}%")
            
            print("\nTASK STATUS:")
            status_count = {"To Do": 0, "In Progress": 0, "Completed": 0}
            for task in self.current_user.team.tasks:
                status_count[task.status] = status_count.get(task.status, 0) + 1
            
            for status, count in status_count.items():
                print(f"  {status}: {count}")
        else:
            print("No team available")
    
    def schedule_meeting(self):
        if not self.current_user.team:
            print("No team available")
            return
        
        print("\nSCHEDULE MEETING")
        date = input("Meeting date/time (e.g., '2025-12-20 14:00'): ").strip()
        platform = input("Platform (Zoom/Teams/Google Meet): ").strip()
        
        meeting = self.current_user.schedule_meeting(date, platform)
        if meeting:
            meeting.send_notification()
    
    def invite_members(self):
        print("\nINVITE MEMBERS")
        emails = input("Enter email addresses (comma-separated): ").strip()
        email_list = [email.strip() for email in emails.split(",") if email.strip()]
    
        if not email_list:
            print("No email addresses provided")
            return
    
        # Call the updated invite_members with access to all_users
        invited_count = 0
        for email in email_list:
            # Find student by email
            found_student = None
            for user in self.all_users:  # Access to platform's all_users
                if isinstance(user, Student) and user.email == email and user != self.current_user:
                    if user not in self.current_user.team.members:
                        found_student = user
                        break
        
            if found_student:
                # Add to team
                self.current_user.team.add_member(found_student)
                found_student.team = self.current_user.team
                print(f"{found_student.name} ({email}) added to team")
                invited_count += 1
            else:
                print(f"Cannot invite {email}: Not found or already in team")
    
        print(f"\n{invited_count} member(s) invited successfully!")
    
    def view_all_tasks(self):
        if self.current_user.team:
            print(f"\nALL TEAM TASKS")
            print("─" * 40)
            for task in self.current_user.team.tasks:
                assigned = task.assigned_to.name if task.assigned_to else "Unassigned"
                print(f"• {task.title}")
                print(f"  Assigned to: {assigned}")
                print(f"  Status: {task.status} | Priority: {task.priority}")
                print()
        else:
            print("No team available")
    
    def remove_member(self):
        if not self.current_user.team:
            print("No team available")
            return
    
        if len(self.current_user.team.members) <= 1:
            print("Cannot remove members - team must have at least 1 member")
            return
    
        print("\nTEAM MEMBERS:")
        print("-" * 30)
        for i, member in enumerate(self.current_user.team.members, 1):
            role = " (Leader)" if member == self.current_user else ""
            # Count member's tasks
            task_count = len([t for t in self.current_user.team.tasks if t.assigned_to == member])
            print(f"{i}. {member.name}{role} - {task_count} task(s)")
    
        try:
            choice = int(input("\nSelect member number to remove (0 to cancel): ").strip())
            if choice == 0:
                print("Operation cancelled")
                return
            
            choice -= 1  # Adjust for zero-based index
        
            if 0 <= choice < len(self.current_user.team.members):
                member = self.current_user.team.members[choice]
            
                if member == self.current_user:
                    print("✗ Cannot remove yourself as leader")
                    print("   Transfer leadership first or disband the team")
                else:
                    # Ask for confirmation
                    confirm = input(f"Are you sure you want to remove {member.name}? (y/n): ").lower()
                    if confirm == 'y':
                        # Unassign any tasks from this member
                        for task in self.current_user.team.tasks:
                            if task.assigned_to == member:
                                task.assigned_to = None
                    
                        # Remove from team
                        self.current_user.team.members.remove(member)
                        member.team = None  # Clear member's team reference
                        print(f"{member.name} removed from team")
                    else:
                        print("Removal cancelled")
            else:
                print(f"Please select a number between 1 and {len(self.current_user.team.members)}")
        except ValueError:
            print("Please enter a valid number")
    
    # ================= LECTURER FUNCTIONS =================
    def view_team_summaries(self):
        print(f"\nALL TEAMS SUMMARY")
        print("─" * 40)
        for team in self.teams:
            self.lecturer.view_group_summary(team)
            print()
    
    def provide_feedback(self):
        if not self.teams:
            print("No teams available")
            return
        
        print("\nPROVIDE FEEDBACK")
        for i, team in enumerate(self.teams, 1):
            print(f"{i}. {team.team_name}")
        
        try:
            choice = int(input("\nSelect team number: ")) - 1
            if 0 <= choice < len(self.teams):
                comment = input("Enter your feedback: ").strip()
                self.current_user.provide_feedback(self.teams[choice], comment)
                print("✓ Feedback submitted")
            else:
                print("Invalid selection")
        except ValueError:
            print("Please enter a valid number")
    
    def view_student_progress(self):
        if not self.teams:
            print("No teams available")
            return
        
        print("\nSTUDENT PROGRESS")
        for team in self.teams:
            print(f"\nTeam: {team.team_name}")
            for member in team.members:
                member_tasks = [task for task in team.tasks if task.assigned_to == member]
                completed = sum(1 for task in member_tasks if task.status == "Completed")
                total = len(member_tasks)
                progress = (completed/total*100) if total > 0 else 0
                print(f"  {member.name}: {completed}/{total} tasks ({progress:.1f}%)")
    
    def generate_reports(self):
        if not self.teams:
            print("No teams available")
            return
        
        print("\nGENERATE REPORTS")
        for i, team in enumerate(self.teams, 1):
            print(f"{i}. {team.team_name}")
        
        try:
            choice = int(input("\nSelect team number: ")) - 1
            if 0 <= choice < len(self.teams):
                report = Report()
                print("\n" + "="*40)
                print(report.generate_project_summary(self.teams[choice]))
                print("\n" + "="*40)
                print(report.generate_member_contribution(self.teams[choice]))
            else:
                print("Invalid selection")
        except ValueError:
            print("Please enter a valid number")
    
    # ================= SYSTEM FUNCTIONS =================
    def view_system_info(self):
        clear_screen()
        print("\n" + "="*60)
        print("SYSTEM INFORMATION".center(60))
        print("="*60)
        
        print(f"\nSYSTEM STATISTICS:")
        print(f"Total Users: {len(self.all_users)}")
        print(f"Total Teams: {len(self.teams)}")
        
        total_tasks = sum(len(team.tasks) for team in self.teams)
        print(f"Total Tasks: {total_tasks}")
        
        print(f"\nUSER ROLES:")
        roles = {"Students": 0, "Group Leaders": 0, "Lecturers": 0}
        for user in self.all_users:
            if isinstance(user, Lecturer):
                roles["Lecturers"] += 1
            elif isinstance(user, GroupLeader):
                roles["Group Leaders"] += 1
            elif isinstance(user, Student):
                roles["Students"] += 1
        
        for role, count in roles.items():
            print(f"{role}: {count}")
        
        print(f"\nACTIVE TEAMS:")
        for team in self.teams:
            progress = team.get_overall_progress()
            print(f"• {team.team_name}")
            print(f"  Members: {len(team.members)} | Tasks: {len(team.tasks)}")
            print(f"  Progress: {progress:.1f}%")
        
        input("\n\nPress Enter to return to main menu...")

    def view_all_students(self):
        """View all students in the system"""
        print("\n📋 ALL STUDENTS IN SYSTEM")
        print("─" * 40)
    
        students = [user for user in self.all_users if isinstance(user, Student)]
    
        if not students:
            print("No students found in system")
            return
    
        for i, student in enumerate(students, 1):
            team_name = student.team.team_name if student.team else "No team"
            task_count = len(student.tasks)
            print(f"{i}. {student.name}")
            print(f"   Email: {student.email}")
            print(f"   Matric: {student.matric_no}")
            print(f"   Team: {team_name}")
            print(f"   Tasks: {task_count}")
            print()

    def validate_task_id_input(self, task_id, user_tasks=None, team_tasks=None):
        """Validate a Task ID input"""
        if not task_id or task_id.strip() == "":
            return False, "Task ID cannot be empty!"
        
        # Check if exists in user's tasks
        if user_tasks:
            if not any(task.task_id == task_id for task in user_tasks):
                return False, f"Task with ID '{task_id}' not found in your tasks"
        
        # Check if exists in team's tasks
        if team_tasks:
            if not any(task.task_id == task_id for task in team_tasks):
                return False, f"Task with ID '{task_id}' not found in team tasks"
        
        return True, "Valid"

    def validate_task_id_unique(self, task_id, team):
        """Check if Task ID is unique in the team"""
        if not task_id or task_id.strip() == "":
            return False, "Task ID cannot be empty!"
        
        if any(task.task_id == task_id for task in team.tasks):
            return False, f"Task ID '{task_id}' already exists in the team"
        
        return True, "Unique"