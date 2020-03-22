package com.zucc.pbcls.pojo.AOE;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ALGraph {
    private List<VNode> vertices = new ArrayList<>();
    private int vernum;
    private int arcnum;
}
