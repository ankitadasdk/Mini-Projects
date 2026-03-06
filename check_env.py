import mediapipe as mp
try:
    print(f"Version: {mp.__version__}")
    print(f"Path: {mp.__file__}")
    print("Solutions found:", hasattr(mp, 'solutions'))
except Exception as e:
    print(f"Error: {e}")
