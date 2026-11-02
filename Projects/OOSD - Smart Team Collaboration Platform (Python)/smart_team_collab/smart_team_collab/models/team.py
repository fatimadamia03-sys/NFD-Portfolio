import datetime

class Team:
    def __init__(self, name, project_details=""):
        self.team_id = f"TEAM-{datetime.datetime.now().strftime('%Y%m%d%H%M')}"
        self.team_name = name
        self.project_details = project_details
        self.members = [] #aggregation from Student class to Team class
                            #We add Student objects to this list, but the students exist independently of the team. 
                            #If I delete the team, the student accounts remain.
        self.tasks = []
        self.meetings = []
        self.comments = []
    
    def add_member(self, student):
        if student not in self.members:
            self.members.append(student)
            print(f"{student.name} added to team")
        else:
            print(f"{student.name} is already in the team")
    
    def remove_member(self, student_id):
        for member in self.members:
            if member.user_id == student_id:
                self.members.remove(member)
                print(f"Member {student_id} removed")
                return True
        print("Member not found")
        return False
    
    def get_overall_progress(self):
        if not self.tasks:
            return 0
        completed = sum(1 for task in self.tasks if task.status == "Completed")
        return (completed / len(self.tasks)) * 100
    
    def get_team_details(self):
        details = f"\nTEAM: {self.team_name}\n"
        details += f"Project: {self.project_details}\n"
        details += f"Progress: {self.get_overall_progress():.1f}%\n"
        details += f"Members: {len(self.members)}\n"
        details += f"Tasks: {len(self.tasks)}\n"
        return details
    
    def __str__(self):
        return f"Team(ID: {self.team_id}, Name: {self.team_name}, Members: {len(self.members)})"