package com.softech.vu360.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.CommissionProduct;
import com.softech.vu360.lms.model.CommissionProductCategory;
import com.softech.vu360.lms.web.controller.administrator.ManageCustomFieldsController;

public class CommissionUtil {

	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
	public List<ProductOrProductCategoryNodeInfo> convertTreeToList (List<TreeNode> treeNodeList, List<ProductOrProductCategoryNodeInfo> nodeInfoList, int level, String indexPrefix)
	{
		ProductOrProductCategoryNodeInfo nodeInfo;
		CommissionProduct commissionProduct;
		CommissionProductCategory commissionProductCategory;
		int x = 0;
		for(TreeNode node : treeNodeList)
		{
			if(node.getValue() != null)
			{
				nodeInfo = new ProductOrProductCategoryNodeInfo();
				if(node.getValue() instanceof CommissionProduct)
				{
					commissionProduct = (CommissionProduct) node.getValue();
					nodeInfo.setId(commissionProduct.getId());
					nodeInfo.setGuid(commissionProduct.getProductCode());
					nodeInfo.setLeaf(true);
					nodeInfo.setName(commissionProduct.getName());
					nodeInfo.setProduct(true);
					nodeInfo.setLevel(level);
					nodeInfo.setIndex(indexPrefix + ++x);
					nodeInfo.setParentId(commissionProduct.getCommissionProductCategory().getProductCategoryCode()+"");
					log.info(StringUtils.leftPad("   ", level) + "commission product - " + nodeInfo.getIndex());
				}
				else
				{
					commissionProductCategory = (CommissionProductCategory) node.getValue();
					nodeInfo.setId(commissionProductCategory.getId());
					nodeInfo.setGuid(commissionProductCategory.getProductCategoryCode());
					nodeInfo.setLeaf(node.isLeaf());
					nodeInfo.setName(commissionProductCategory.getName());
					nodeInfo.setProduct(false);
					nodeInfo.setLevel(level);
					nodeInfo.setIndex(indexPrefix + ++x);
					if(commissionProductCategory.getParentCommissionProductCategory()!=null)
						nodeInfo.setParentId(commissionProductCategory.getParentCommissionProductCategory().getProductCategoryCode()+"");
					else
						nodeInfo.setParentId("0");
					log.info(StringUtils.leftPad("   ", level) + "commission product category - " + nodeInfo.getIndex());
				}
				nodeInfoList.add(nodeInfo);
				if(node.getChildCount() > 0)
					convertTreeToList(node.getChildren(), nodeInfoList, level + 1, indexPrefix + x);
			}
			else
				log.info(StringUtils.leftPad("   ", level) + "node value is null");
		}
		return nodeInfoList;
	}
	
}
