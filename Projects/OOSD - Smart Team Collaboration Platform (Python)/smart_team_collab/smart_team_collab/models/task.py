class Task:
    def __init__(self, task_id, title, deadline, priority="Medium"):
        # Validate inputs
        if not task_id or task_id.strip() == "":
            raise ValueError("Task ID cannot be empty!")
        if not title or title.strip() == "":
            raise ValueError("Task title cannot be empty!")
        
        self.task_id = task_id
        self.title = title
        self.deadline = deadline
        self.priority = priority
        self.status = "To Do"
        self.assigned_to = None
        self.comments = []
    
    def add_comment(self, comment, author="System"):
        if not comment or comment.strip() == "":
            print("Comment cannot be empty!")
            return False
        
        from .comment import Comment
        comment_obj = Comment(comment, author)
        self.comments.append(comment_obj)
        print(f"Comment added to task '{self.title}'")
        return True
    
    def edit(self, **kwargs): #method overloading - **kwargs to allow editing a task with just a title, or with a title, deadline, and priority.
        for key, value in kwargs.items():
            if hasattr(self, key):
                # Validate task_id if being edited
                if key == 'task_id' and (not value or value.strip() == ""):
                    print("Task ID cannot be empty!")
                    continue
                setattr(self, key, value)
        print(f"Task '{self.title}' updated")
    
    def mark_completed(self):
        self.status = "Completed"
        print(f"Task '{self.title}' marked as completed")
    
    def __str__(self):
        return f"Task(ID: {self.task_id}, Title: {self.title}, Status: {self.status})"