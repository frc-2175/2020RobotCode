package frc.math;

import javax.swing.text.Segment;

public class DrivingUtility {
    
    public static double getTrapezoidSpeed(
        double startSpeed,
        double middleSpeed,
        double endSpeed, 
        double totalDistance,
        double rampUpDistance,
        double rampDownDistance,
        double currentDistance
    ) {
        if (rampDownDistance + rampUpDistance > totalDistance) {
            if (currentDistance < 0) {
                return startSpeed; 
            } else if (currentDistance > totalDistance) {
                return endSpeed; 
            }

            return MathUtility.lerp(startSpeed, endSpeed, currentDistance/totalDistance);
        } 

        if (currentDistance < 0) {
            return startSpeed; 
        } else if (currentDistance < rampUpDistance) {
            return MathUtility.lerp(startSpeed, middleSpeed, currentDistance/rampUpDistance); 
        } else if (currentDistance < totalDistance - rampDownDistance) {
            return middleSpeed; 
        } else if (currentDistance < totalDistance) {
            return MathUtility.lerp(middleSpeed, endSpeed, (currentDistance - (totalDistance - rampDownDistance)) / rampDownDistance);
        } else {
            return endSpeed; 
        }
    }

    public static class PathSegment {
        public double angle;
        public Vector[] path;

        public PathSegment(double angle, Vector[] path) {
            this.angle = angle;
            this.path = path;
        }
    }

    public static Vector[] makePathLine(Vector startPoint, Vector endPoint) {
        int numPoints = (int) Math.sqrt(Math.pow(endPoint.x - startPoint.x, 2) + Math.pow(endPoint.y - startPoint.y, 2)) + 2;
        Vector pathVector = endPoint.subtract(startPoint).normalized();
        Vector[] path = new Vector[numPoints];
        for (int i = 0; i < numPoints - 1; i++) {
            path[i] = startPoint.add(pathVector.multiply(i));
        }
        path[path.length - 1] = endPoint; 
        return path;
    }

    public static PathSegment makeLinePathSegment(double distance) {
        return new PathSegment(0, makePathLine(new Vector(0, 0), new Vector(0, distance)));
    }

    public static PathSegment makeRightArcPathSegment(double radius, double degrees) {
        double circumfrence = 2.0 * Math.PI * radius;
        double distanceOfPath = circumfrence * ( degrees / 360 );
        double yEndpoint = radius * Math.sin(Math.toRadians(degrees));
        double xEndpoint = radius - (radius * Math.cos(Math.toRadians(degrees)));
        double degreesPerInch = 360 / circumfrence;
        int numpoints = (int) distanceOfPath + 2;
        Vector[] path = new Vector[numpoints];
        for (int i = 0; i < numpoints - 1; i++) {
            double angle = i * degreesPerInch;
            double yPosition = radius * Math.sin(Math.toRadians(angle));
            double xPosition = radius - (radius * Math.cos(Math.toRadians(angle)));
            path[i] = new Vector(xPosition, yPosition);
        }
        path[numpoints - 1] = new Vector(xEndpoint, yEndpoint);
        return new PathSegment(-degrees, path);
    }
    

    public static PathSegment makeLeftArcPathSegment(double radius, double degrees) {
        Vector[] rightPath = makeRightArcPathSegment(radius, degrees).path;
        Vector[] leftPath = new Vector[rightPath.length];
        for (int i = 0; i < rightPath.length; i++) {
            leftPath[i] = new Vector(-rightPath[i].x, rightPath[i].y);
        }
        return new PathSegment(degrees, leftPath);
    }
}