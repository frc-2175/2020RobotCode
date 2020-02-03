import cv2
import numpy
import math


hue = (63.129496402877685, 103.5993208828523)
saturation = (43.57014388489208, 255.0)
value = (91.72661870503595, 255.0)
minArea = 200

def process(img):
    img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    img = cv2.inRange(img, (hue[0], saturation[0], value[0]),  (hue[1], saturation[1], value[1]))
    contours, hierarchy = cv2.findContours(img, mode = cv2.RETR_EXTERNAL, method = cv2.CHAIN_APPROX_SIMPLE)
    
    biggestContour = None
    maxAreaSoFar = 0
    for contour in contours: 
        area = cv2.contourArea(contour)
        if area >= minArea:
            if area > maxAreaSoFar:
                maxAreaSoFar = area
                biggestContour = contour
    if biggestContour is None:
        return img, None
    
    biggestContour = cv2.convexHull(biggestContour)
    shapeImage = numpy.zeros(img.shape)
    cv2.drawContours(shapeImage, [biggestContour], -1, (255,255,255), cv2.FILLED)
    return shapeImage, biggestContour

