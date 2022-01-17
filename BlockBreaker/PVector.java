class PVector{
    float x, y, z;

    PVector(float x, float y){this(x, y, 0);}

    PVector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PVector clone(){
        return new PVector(x, y, z);
    }

    public float mag(){
        return (float)Math.sqrt(x*x+y*y+z*z);
    }

    public PVector normalized(){
        return div(mag());
    }

    public PVector setMag(float v){
        return div(mag()/v);
    }

    public PVector add(PVector p){
        x += p.x;
        y += p.y;
        z += p.z;
        return this;
    }

    public PVector sub(PVector p){
        x -= p.x;
        y -= p.y;
        z -= p.z;
        return this;
    }

    public PVector mult(float v){
        x *= v;
        y *= v;
        z *= v;
        return this;
    }

    public PVector div(float v){
        return mult(1.0f/v);
    }
}