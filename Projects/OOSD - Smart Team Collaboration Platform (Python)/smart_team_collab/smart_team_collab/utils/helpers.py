import os
import re
import datetime

def clear_screen():
    """Clear console screen - works on both Windows and Unix"""
    os.system('cls' if os.name == 'nt' else 'clear')

def validate_date(date_string, date_format="%Y-%m-%d"):
    """Validate date format"""
    try:
        datetime.datetime.strptime(date_string, date_format)
        return True
    except ValueError:
        return False

def validate_email(email):
    """Simple email validation"""
    pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
    return re.match(pattern, email) is not None

def print_header(title):
    """Print a formatted header"""
    print("\n" + "="*60)
    print(title.center(60))
    print("="*60)

def print_section(title):
    """Print a section header"""
    print(f"\n{title}")
    print("-" * len(title))