package info.realjin.newsintime.service;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.PredefinedCategory;
import info.realjin.newsintime.domain.PredefinedCollectionItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.app.Application;
import android.util.Log;

/**
 * must init after appdata!
 * 
 * @author realjin
 * 
 * 
 */
public class PredefinedDataManagerService {

	private NewsInTimeApp app;
	private boolean loaded;

	public PredefinedDataManagerService(NewsInTimeApp app) {
		this.app = app;
		this.loaded = false;

	}

	public void loadPredefinedToAppdata() {
		Log.e("PredefinedDataManagerService", "000");

		AppData data = app.getData();
		// clear
		data.getPdCategoryMap().clear();
		data.getPdCategoryList().clear();
		data.getPdCiList().clear();

		InputStream is = app.getResources().openRawResource(R.raw.predefined);
		try {
			if (is == null) {
				return;
			}
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(is);
			Element root = doc.getRootElement();

			Log.e("PredefinedDataManagerService", "1111");
			// parse category list
			Element elCategoryList = root.element("category-list");
			Iterator itCategory = elCategoryList.elementIterator("category");
			while (itCategory.hasNext()) {
				Log.e("PredefinedDataManagerService", "itCategory");
				Element elCategory = (Element) itCategory.next();
				PredefinedCategory c = new PredefinedCategory();
				c.setId(elCategory.attributeValue("id"));
				c.setName(elCategory.getTextTrim());
				data.getPdCategoryMap().put(c.getName(), c.getId());
				data.getPdCategoryList().add(c);
			}

			Log.e("PredefinedDataManagerService", "2222");
			// parse ci list
			Element elCiList = root.element("ci-list");
			Iterator itCi = elCiList.elementIterator("ci");
			while (itCi.hasNext()) {
				Log.e("PredefinedDataManagerService", "itCi");
				Element elCi = (Element) itCi.next();
				PredefinedCollectionItem ci = new PredefinedCollectionItem();
				String catId = elCi.attributeValue("category");
				String catName = data.getPdCategoryMap().get(catId);
				if (catId == null) {
					Log.e("loadPredefined", "cilist parsing error: cat \""
							+ elCi.attributeValue("category") + "\" not found");
					continue;
				}
				ci.setUrl(elCi.attributeValue("url"));
				ci.setName(elCi.elementText("name"));
				ci.setCategoryId(catId);
				ci.setCategoryName(catName);
				data.getPdCiList().add(ci);
			}
			loaded = true;

			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (DocumentException e) {
			data.getPdCiList().clear();
			data.getPdCategoryMap().clear();
			data.getPdCiList().clear();
			loaded = false;
			Log.e("PredefinedDataManagerService", e.getMessage());
		}
	}

}
