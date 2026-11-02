import datetime

class Report:
    def __init__(self):
        self.generated_date = datetime.datetime.now()
        self.report_content = ""
    
    def generate_member_contribution(self, team):
        report = f"\nMEMBER CONTRIBUTION REPORT\n"
        report += "="*40 + "\n"
        report += f"Team: {team.team_name}\n"
        report += f"Date: {self.generated_date.strftime('%Y-%m-%d')}\n\n"
        
        for member in team.members:
            member_tasks = [task for task in team.tasks if task.assigned_to == member]
            completed = sum(1 for task in member_tasks if task.status == "Completed")
            total = len(member_tasks)
            
            if total > 0:
                percentage = (completed / total) * 100
            else:
                percentage = 0
            
            report += f"{member.name}:\n"
            report += f"  Tasks: {total}\n"
            report += f"  Completed: {completed}\n"
            report += f"  Rate: {percentage:.1f}%\n"
            report += "-" * 30 + "\n"
        
        self.report_content = report
        return report
    
    def generate_project_summary(self, team):
        report = f"\nPROJECT SUMMARY REPORT\n"
        report += "="*40 + "\n"
        report += f"Team: {team.team_name}\n"
        report += f"Date: {self.generated_date.strftime('%Y-%m-%d')}\n"
        report += f"Project: {team.project_details}\n\n"
        
        total = len(team.tasks)
        completed = sum(1 for task in team.tasks if task.status == "Completed")
        progress = team.get_overall_progress()
        
        report += f"Total Tasks: {total}\n"
        report += f"Completed: {completed}\n"
        report += f"Progress: {progress:.1f}%\n"
        
        self.report_content = report
        return report
    
    def save_to_file(self, filename):
        with open(filename, 'w') as f:
            f.write(self.report_content)
        print(f"Report saved to {filename}")