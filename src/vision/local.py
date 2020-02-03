import cv2

from pipeline import process


def main():
    cam = cv2.VideoCapture(0)
    while True:
        ret_val, img = cam.read()
        img = cv2.flip(img, 1)
        img, contour = process(img)        
        cv2.imshow('my webcam', img)
        if cv2.waitKey(1) == 27: 
            break  # esc to quit
    cv2.destroyAllWindows()


if __name__ == '__main__':
    main()