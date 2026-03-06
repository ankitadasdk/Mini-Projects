import cv2
import mediapipe as mp
import numpy as np
import math


mp_pose = mp.solutions.pose
pose = mp_pose.Pose(static_image_mode=False, min_detection_confidence=0.5)
mp_drawing = mp.solutions.drawing_utils
shirt = cv2.imread('shirt.jpg', cv2.IMREAD_UNCHANGED)
cap = cv2.VideoCapture(0)
while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break
    image_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = pose.process(image_rgb)

    if results.pose_landmarks:
        landmarks = results.pose_landmarks.landmark
        h, w, _ = frame.shape
        l_shoulder = landmarks[mp_pose.PoseLandmark.LEFT_SHOULDER]
        r_shoulder = landmarks[mp_pose.PoseLandmark.RIGHT_SHOULDER]
        ls_x, ls_y = int(l_shoulder.x * w), int(l_shoulder.y * h)
        rs_x, rs_y = int(r_shoulder.x * w), int(r_shoulder.y * h)
        dist = math.sqrt((ls_x - rs_x)**2 + (ls_y - rs_y)**2)
        angle = math.degrees(math.atan2(rs_y - ls_y, rs_x - ls_x))
        cv2.line(frame, (ls_x, ls_y), (rs_x, rs_y), (0, 255, 0), 3)
        cv2.putText(frame, f"Angle: {int(angle)} deg", 
                    (rs_x, rs_y - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 
                    0.6, (0, 255, 0), 2)
        mp_drawing.draw_landmarks(
            frame,
            results.pose_landmarks,
            mp_pose.POSE_CONNECTIONS
        )
    cv2.imshow('AuraFit - Tracking Phase', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
