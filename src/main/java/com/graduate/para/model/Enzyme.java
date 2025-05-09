package com.graduate.para.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName enzyme
 */
@TableName(value ="enzyme")
@Data
public class Enzyme implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * kcat value
     */
    private Double kcat;

    /**
     * 底物名称
     */
    private String sub;

    /**
     * 底物SMILES结构
     */
    private String smiles;

    /**
     * 酶的氨基酸序列
     */
    private String sequences;

    /**
     * 是否为预测值: 0-真实值, 1-UniKP预测, 2-DLTKcat预测, 3-DLKcat预测
     */
    private Integer predicted;

    /**
     * 温度参数（摄氏度）
     */
    private Double temperature;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Enzyme other = (Enzyme) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getKcat() == null ? other.getKcat() == null : this.getKcat().equals(other.getKcat()))
            && (this.getSub() == null ? other.getSub() == null : this.getSub().equals(other.getSub()))
            && (this.getSmiles() == null ? other.getSmiles() == null : this.getSmiles().equals(other.getSmiles()))
            && (this.getSequences() == null ? other.getSequences() == null : this.getSequences().equals(other.getSequences()))
            && (this.getPredicted() == null ? other.getPredicted() == null : this.getPredicted().equals(other.getPredicted()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()))
            && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()))
            && (this.getUpdatedTime() == null ? other.getUpdatedTime() == null : this.getUpdatedTime().equals(other.getUpdatedTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getKcat() == null) ? 0 : getKcat().hashCode());
        result = prime * result + ((getSub() == null) ? 0 : getSub().hashCode());
        result = prime * result + ((getSmiles() == null) ? 0 : getSmiles().hashCode());
        result = prime * result + ((getSequences() == null) ? 0 : getSequences().hashCode());
        result = prime * result + ((getPredicted() == null) ? 0 : getPredicted().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdatedTime() == null) ? 0 : getUpdatedTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", kcat=").append(kcat);
        sb.append(", sub=").append(sub);
        sb.append(", smiles=").append(smiles);
        sb.append(", sequences=").append(sequences);
        sb.append(", predicted=").append(predicted);
        sb.append(", temperature=").append(temperature);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
} 