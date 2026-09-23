package com.wechuang.mallshop.common.config.mybatis;

import com.wechuang.mallshop.common.config.Point;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.*;
import java.util.Arrays;

public class PointTypeHandler extends BaseTypeHandler<Point> {

    // 将 Point 对象转换为 SQL 参数
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Point parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();

        String wkt = String.format("POINT(%f %f)", parameter.getX(), parameter.getY());
        String sql = "SELECT ST_GeomFromText(?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, wkt);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Object obj = rs.getObject(1);
                ps.setObject(i, obj);
            } else {
                throw new SQLException("Failed to convert Geometry to database type");
            }
        }
    }


    // 从 ResultSet 获取 Point 对象
    @Override
    public Point getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte[] pointData = rs.getBytes(columnName);
        return parseBinaryPoint(pointData);
    }

    // 从 ResultSet 获取 Point 对象
    @Override
    public Point getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        byte[] pointData = rs.getBytes(columnIndex);
        return parseBinaryPoint(pointData);
    }

    // 从 CallableStatement 获取 Point 对象
    @Override
    public Point getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte[] pointData = cs.getBytes(columnIndex);
        return parseBinaryPoint(pointData);
    }

    private Point parseBinaryPoint(byte[] pointData) {
        if (pointData != null) {
            System.out.println("Raw binary data: " + Arrays.toString(pointData)); // 打印字节数据

            int idx = 0;
            if (pointData.length != 16) {
                idx =  pointData.length - 16;
            }

            ByteBuffer buffer = ByteBuffer.wrap(pointData);
            buffer.order(ByteOrder.LITTLE_ENDIAN); // 确保字节顺序是小端
            buffer.position(idx);

            double x = buffer.getDouble();  // 获取 x 坐标
            double y = buffer.getDouble();  // 获取 y 坐标
            System.out.println("Parsed Point - x: " + x + ", y: " + y); // 打印解析结果
            return new Point(x, y);
        }
        return null;  // 如果数据无效，返回 null
    }

}
