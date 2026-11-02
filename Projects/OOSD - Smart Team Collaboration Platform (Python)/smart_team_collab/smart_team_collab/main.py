"""
SMART STUDENT TEAM COLLABORATION PLATFORM
Team Members:
1. NUR FATIMA DAMIA BINTI MOHAMMAD YAZIS (107468)
2. MOHAMAD HAZIM AIMAN BIN YUSOF (105014)
3. Kiew Sie Wung (104671)
4. SALLEN LIU YEE LING (100925)
"""

import sys
import os

# Add the current directory to the Python path
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from collaboration_platform import CollaborationPlatform

def main():
    """Main entry point of the application"""
    platform = CollaborationPlatform()
    
    
    try:
        platform.main_menu()
    except KeyboardInterrupt:
        print("\n\nProgram interrupted. Goodbye!")
    except Exception as e:
        print(f"\nAn error occurred: {e}")
        import traceback
        traceback.print_exc()
        input("\nPress Enter to exit...")

if __name__ == "__main__":
    main()