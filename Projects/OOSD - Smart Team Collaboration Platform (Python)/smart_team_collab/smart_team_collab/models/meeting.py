class Meeting:
    def __init__(self, meeting_id, meeting_time, platform):
        self.meeting_id = meeting_id
        self.meeting_time = meeting_time
        self.platform = platform
    
    def send_notification(self):
        print(f"Meeting notification sent: {self.meeting_id} at {self.meeting_time}")
    
    def reschedule(self, new_time):
        self.meeting_time = new_time
        print(f"Meeting rescheduled to {new_time}")
    
    def __str__(self):
        return f"Meeting(ID: {self.meeting_id}, Time: {self.meeting_time}, Platform: {self.platform})"