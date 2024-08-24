package com.bbyy.weeat.ui.view.bubble;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bbyy.weeat.R;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.Random;

public class Bubble {
    private final Random random = new Random();
    private World world;
    private float friction = 0.3f,density,restitution = 0.3f,ratio = 50;
    private int width,height;
    private boolean enable = true;
    private final ViewGroup viewGroup;

    public Bubble(ViewGroup viewGroup) {
        this.viewGroup=viewGroup;
        //获取屏幕像素密度
        density = viewGroup.getContext().getResources().getDisplayMetrics().density;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        if(friction >= 0){
            this.friction = friction;
        }
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        if(density >= 0){
            this.density = density;
        }
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        if(restitution >= 0){
            this.restitution = restitution;
        }
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        if(ratio >= 0){
            this.ratio = ratio;
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        //刷新重绘视图
        viewGroup.invalidate();
    }

    public void onLayout(boolean changed){
        createWorld(changed);
    }

    public void onStart(){
        setEnable(true);
    }

    public void onStop(){
        setEnable(false);
    }

    public void update(){
        //重新构建world
        world = null;
        onLayout(true);
    }

    public void onSizeChanged(int w,int h){
        //sizeChanged的时候获取到viewgroup的宽和高
        this.width = w;
        this.height = h;
    }

    public void onDraw(Canvas canvas){
        if(!enable){ //设置标记，在界面可见的时候开始draw，在界面不可见的时候停止draw
            return;
        }
        //dt 更新引擎的间隔时间
        //velocityIterations 计算速度
        //positionIterations 迭代的次数
        float dt = 1f / 60f;
        int velocityIterations = 3;
        int positionIterations = 10;
        world.step(dt, velocityIterations, positionIterations);
        int childCount = viewGroup.getChildCount();
        for(int i = 0; i < childCount; i++){
            View view = viewGroup.getChildAt(i);
            Body body = (Body) view.getTag(R.id.bubble_body_tag);
            if(body != null){
                //从view中获取绑定的刚体，取出参数，开始更新view（view的x和y实际设置的是左上角的点）
                view.setX(metersToPixels(body.getPosition().x) - view.getWidth() / 2);
                view.setY(metersToPixels(body.getPosition().y) - view.getHeight() / 2);
                view.setRotation(radiansToDegrees(body.getAngle() % 360));
            }
        }
        //手动调用刷新重回视图，反复执行onDraw方法
        viewGroup.invalidate();
    }

    private void createWorld(boolean changed) {
        //jbox2d中world称为世界，这里创建一个世界,TODO：开屏息屏可能会创建两次bug
        Log.d("test"," world created");
        if(world == null){
            //设置物理世界的重力,Vec2 表示一个二维向量， (0, 10.0f) 表示重力在 x 轴上的分量为 0，而在 y 轴上的分量为 10.0f。
            world = new World(new Vec2(0f, 0f));
            //创建边界，注意边界为static静态的，当物体触碰到边界，停止模拟该物体
            createTopAndBottomBounds();
            createLeftAndRightBounds();
        }
        int childCount = viewGroup.getChildCount();
        for(int i = 0; i < childCount; i++){
            View view = viewGroup.getChildAt(i);
            // 拿到气泡view
            Body body = (Body) view.getTag(R.id.bubble_body_tag);
            if(body == null){
                createBody(world,view);
            }
        }
    }

    private void createTopAndBottomBounds() {
        BodyDef bodyDef = new BodyDef();
        //静态刚体
        bodyDef.type = BodyType.STATIC;

        PolygonShape box = new PolygonShape();
        float boxWidth = pixelsToMeters(width);
        float boxHeight = 0;
        box.setAsBox(boxWidth, boxHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape=box;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;

        bodyDef.position.set(0, -boxHeight);
        Body topBody = world.createBody(bodyDef);
        topBody.createFixture(fixtureDef);

        bodyDef.position.set(0, pixelsToMeters(height)+boxHeight);
        Body bottomBody = world.createBody(bodyDef);
        bottomBody.createFixture(fixtureDef);
    }

    // 创建左右边界
    private void createLeftAndRightBounds() {
        float boxWidth = pixelsToMeters(ratio);
        float boxHeight = pixelsToMeters(height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape box = new PolygonShape();
        box.setAsBox(boxWidth, boxHeight);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape=box;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;

        bodyDef.position.set(-boxWidth, boxHeight);
        Body leftBody = world.createBody(bodyDef);
        leftBody.createFixture(fixtureDef);


        bodyDef.position.set(pixelsToMeters(width) + boxWidth, 0);
        Body rightBody = world.createBody(bodyDef);
        rightBody.createFixture(fixtureDef);
    }


    private Shape createCircleShape(View view){
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(pixelsToMeters(view.getWidth() / 2));
        return circleShape;
    }

    private Shape createPolygonShape(View view){
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(pixelsToMeters(view.getWidth() / 2),pixelsToMeters(view.getHeight() / 2));
        return polygonShape;
    }

    private void createBody(World world, View view) {
        //创建刚体描述，因为刚体需要随重力运动，这里type设置为DYNAMIC
        BodyDef bodyDef = new BodyDef();
        bodyDef.type=BodyType.DYNAMIC;

        //设置初始参数，为view的中心点
        bodyDef.position.set(pixelsToMeters(view.getX() + view.getWidth() / 2) ,
                pixelsToMeters(view.getY() + view.getHeight() / 2));
        Shape shape;
        Boolean isCircle = (Boolean) view.getTag(R.id.bubble_view_circle_tag);
        if(isCircle != null && isCircle){
            //创建圆体形状
            shape = createCircleShape(view);
        }else{
            //创建多边形形状
            shape = createPolygonShape(view);
        }

        //初始化物体信息
        FixtureDef fixture = new FixtureDef();
        fixture.shape=shape;
        fixture.friction = friction;    //friction  物体摩擦力
        fixture.restitution = restitution;  //restitution 物体恢复系数
        fixture.density = density;  //density 物体密度

        //用世界创建出刚体
        Body body = world.createBody(bodyDef);
        body.createFixture(fixture);
        view.setTag(R.id.bubble_body_tag,body);

        //  初始化物体的运动行为
        //  setLinearVelocity用于设置刚体的线性速度。
        //  new Vec2(random.nextFloat(), random.nextFloat()) 创建了一个二维向量，其中 x 和 y 分量都是随机生成的浮点数。
        //  这里的 random.nextFloat() 会返回一个介于 0.0 和 1.0 之间的随机浮点数.
        //  因此，这行代码会瞬间将刚体的速度设置为一个随机的方向和大小
        body.setLinearVelocity(new Vec2(random.nextFloat(),random.nextFloat()));
    }

    //jbox2d中，物体位置以meter确定，需要进行一个像素和meter之间的转换
    public float metersToPixels(float meters) {
        return meters * ratio;
    }

    public float pixelsToMeters(float pixels) {
        return pixels / ratio;
    }

    //弧度转角度
    private float radiansToDegrees(float radians) {
        return radians / 3.14f * 180f;
    }

    //角度转弧度
    private float degreesToRadians(float degrees){
        return (degrees / 180f) * 3.14f;
    }

    public void random() {
        //弹一下，模拟运动
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Vec2 impulse = new Vec2(random.nextInt(1000) - 1000, random.nextInt(1000) - 1000);
            View view = viewGroup.getChildAt(i);
            Body body = (Body) view.getTag(R.id.bubble_body_tag);
            if(body != null){
                body.applyLinearImpulse(impulse, body.getPosition());
            }
        }
    }

    public void onSensorChanged(float x,float y) {
        //传感器模拟运动
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Vec2 impulse = new Vec2(x, y);
            View view = viewGroup.getChildAt(i);
            Body body = (Body) view.getTag(R.id.bubble_body_tag);
            if(body != null){
                body.applyLinearImpulse(impulse, body.getPosition());
            }
        }
    }
}
