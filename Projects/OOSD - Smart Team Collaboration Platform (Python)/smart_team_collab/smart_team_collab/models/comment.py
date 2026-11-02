import datetime

#encapsulation - Data (content, author, timestamp) is bundled together
class Comment:
    def __init__(self, content, author):
        self.content = content
        self.author = author
        self.timestamp = datetime.datetime.now()
    
    def get_formatted_comment(self):
        return f"[{self.timestamp.strftime('%H:%M')}] {self.author}: {self.content}"