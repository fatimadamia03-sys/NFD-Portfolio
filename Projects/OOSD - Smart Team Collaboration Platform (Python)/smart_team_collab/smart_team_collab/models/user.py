from .task import Task

class User: #inheritance (parent)
    def __init__(self, user_id, name, email, password): #attributes class User
        self.user_id = user_id
        self.name = name
        self.email = email
        self.password = password
    
    #method login
    def login(self, user_id, password):
        return self.user_id == user_id and self.password == password
    
    #method logout
    def logout(self):
        print(f"{self.name} logged out")
    
    #method displayMenu
    def display_menu(self): #polymorphism(general)- OVERRIDEN by children class to show different menus
        print("\n" + "="*40)
        print("USER MENU")
        print("="*40)
        print("1. View Profile")
        print("0. Logout")
    
    def get_profile_info(self):
        return f"Name: {self.name}\nEmail: {self.email}\nID: {self.user_id}"


class Student(User): #inheritance (child)
    def __init__(self, user_id, name, email, password, matric_no, course): #attributes class Student
        super().__init__(user_id, name, email, password) #automatically gets userID, name, email, pass from User class
        self.matric_no = matric_no
        self.course = course
        self.tasks = []
        self.team = None
    
    #polymorphism - overriding
    def display_menu(self): #replaces the generic User menu with specific options like "View My Tasks".
        print("\n" + "="*50)
        print(f"STUDENT MENU: {self.name}")
        print("="*50)
        print("1. View My Tasks")
        print("2. Update Task Status")
        print("3. View Team Information")
        print("4. Add Time to Task")
        print("5. Add File to Task")
        print("6. View Profile")
        print("0. Logout")
    
    #method updateTaskStatus
    def update_task_status(self, task_id, status):
        if not task_id or task_id.strip() == "":
            print("Task ID cannot be empty!")
            return False
        
        for task in self.tasks:
            if task.task_id == task_id:
                task.status = status
                print(f"Task '{task.title}' status updated to '{status}'")
                return True
        
        print(f"Task with ID '{task_id}' not found in your tasks")
        return False
    
    #method joinTeam
    def join_team(self, team):
        if self.team:
            print(f"Already in team: {self.team.team_name}")
            return False
        self.team = team
        team.add_member(self)
        return True
    
    def add_time_to_task(self, task_id, time_spent):
        print(f"Added {time_spent} to task {task_id}")
    
    def add_file_to_task(self, task_id, filename):
        print(f"File '{filename}' added to task {task_id}")
    
    def get_profile_info(self):
        base_info = super().get_profile_info()
        return f"{base_info}\nMatric No: {self.matric_no}\nCourse: {self.course}\nTeam: {self.team.team_name if self.team else 'None'}"


class GroupLeader(Student): #deep inheritance (grandchild)
    def __init__(self, user_id, name, email, password, matric_no, course): #attributes class GroupLeader
        super().__init__(user_id, name, email, password, matric_no, course) #It inherits from Student, which inherits from User. It gets everything (User's login + Student's task lists) plus its own powers.
    
    #polymorphism - overriding
    def display_menu(self):
        print("\n" + "="*50)
        print(f"GROUP LEADER MENU: {self.name}")
        print("="*50)
        print("1. Create Team")
        print("2. Assign Task")
        print("3. View Team Progress")
        print("4. Schedule Meeting")
        print("5. Invite Members")
        print("6. View All Tasks")
        print("7. Remove Member")
        print("0. Logout")
    
    #method createTeam - Initializes the Team object.
    def create_team(self, name, details=""):
        from .team import Team
        team = Team(name, details)
        team.add_member(self)
        self.team = team
        print(f"Team '{name}' created")
        return team
    
    #method assignTask
    def assign_task(self, task, student):
        task.assigned_to = student
        student.tasks.append(task) #Connects a Task object to a Student object.
        print(f"Task '{task.title}' assigned to {student.name}")
    
    #method scheduleMeeting
    def schedule_meeting(self, date, platform):
        from .meeting import Meeting
        meeting = Meeting(f"MTG-{self.team.team_id}", date, platform)
        self.team.meetings.append(meeting)
        print(f"Meeting scheduled on {date} via {platform}")
        return meeting
    
    #method inviteMember
    def invite_members(self, members_list):
        for member in members_list:
            print(f"Invitation sent to {member}")


class Lecturer(User): #inheritance (child)-from User class
    def __init__(self, user_id, name, email, password, staff_id, department): #attributes lecturer class
        super().__init__(user_id, name, email, password) #reuse attributes inherit from user
        self.staff_id = staff_id
        self.department = department
    
    #polymorphism - overriding
    def display_menu(self): 
        print("\n" + "="*50)
        print(f"LECTURER MENU: {self.name}")
        print("="*50)
        print("1. View Team Summaries")
        print("2. Provide Feedback")
        print("3. View Student Progress")
        print("4. Generate Reports")
        print("0. Logout")
    
    #method viewGroupSummary
    def view_group_summary(self, team):
        print(f"\n Team Summary: {team.team_name}")
        print(f"   Project: {team.project_details}")
        print(f"   Members: {len(team.members)}")
        print(f"   Progress: {team.get_overall_progress():.1f}%")
    
    #method provideFeedback
    def provide_feedback(self, team, comment_text):
        from .comment import Comment
        comment = Comment(comment_text, self.name)
        team.comments.append(comment)
        print(f"Feedback provided to {team.team_name}")
    
    def get_profile_info(self):
        base_info = super().get_profile_info()
        return f"{base_info}\nStaff ID: {self.staff_id}\nDepartment: {self.department}"