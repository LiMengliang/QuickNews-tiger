package com.tiger.quicknews.html;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import android.R;
import android.content.Context;

import com.tiger.quicknews.bean.ImageDetailModel;
import com.tiger.quicknews.bean.NewsDetailModel;
import com.tiger.quicknews.exception.ConvertionException;

public class DetailNewsGenerator {

	private static DetailNewsGenerator _detailNewsGenerator = null;
	Context context;
	
	public DetailNewsGenerator(Context context)
	{
		this.context = context;
	}
	
	public String generateNewsDetailHtml(NewsDetailModel newsDetailModel) throws IOException, ConvertionException
	{
		BufferedReader templateReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("news_detail_template.html")));
		StringBuilder newsDetailHtml = new StringBuilder();
		String line;
		while((line = templateReader.readLine()) != null)
		{
			newsDetailHtml.append(line);
		}		
		List<ImageDetailModel> imageDetailModels = newsDetailModel.getImgList();
		String body = newsDetailModel.getBody();
		for(ImageDetailModel imageDetailModel : imageDetailModels)
		{
			if(!body.contains(imageDetailModel.ref))
			{
				throw new ConvertionException();
			}
			StringBuilder imageHref = new StringBuilder();
			imageHref.append("<img src=\"");
			imageHref.append(imageDetailModel.src);
			imageHref.append("\"");
			imageHref.append(" width=340 />");			
			body = body.replace(imageDetailModel.ref, imageHref);
		}		
		return newsDetailHtml.toString().replace("<!--content-->", body);
	}
	
//	public static String generateNewsDetail(String url) throws IOException
//	{
//		BufferedReader templateReader = new BufferedReader(new InputStreamReader(new FileInputStream("file:///assets/news_detail_template.html"), "UTF-8"));
//		StringBuilder content = new StringBuilder();
//		String line;
//		while((line = templateReader.readLine()) != null)
//		{
//			content.append(line);
//		}
//		Document templateDocuemnt = Jsoup.parse(content.toString());
//		Element templateContentElement = templateDocuemnt.getElementsByClass("content").first();
//		
//		templateContentElement.insertChildren(0, formatDetailNewsContent(url));
//		return templateDocuemnt.html();
//	}
//	
//	private static Elements formatDetailNewsContent(String newsUrl) throws IOException
//	{
//		Document document = null;
//		try
//		{
//			document = Jsoup.connect(newsUrl).timeout(50000).get();
//			
//		}
//		catch(SocketTimeoutException e)
//		{	
//			return new Elements();
//		}
//		catch(IOException e)
//		{
//			return new Elements();
//		}
//		Elements allHref = document.getElementsByTag("a");
//		Element hrefToRest = null;
//		for(Element href : allHref)
//		{
//			if(href.text().equals("查看全文"))
//			{
//				hrefToRest = href;
//				break;
//			}
//		}
//		if(hrefToRest != null)
//		{
//			String linkToRest = hrefToRest.attr("href");
//			return formatDetailNewsContent(linkToRest);
//		}
//		String head = document.head().toString();
//		String body = document.body().toString();
//		Elements ListDiv = document.getElementsByAttributeValue("class","content");
//		Element contentElement = ListDiv.first();
//		if(contentElement != null)
//		{
//			Elements allLinks = contentElement.getElementsByTag("a");
//			for(Element link : allLinks)
//			{
//				if(link.text().equals("查看大图"))
//				{
//					String linkToLargeImage = link.attr("href");
//					try
//					{
//						Document largeImageDocument = Jsoup.connect(linkToLargeImage).timeout(10000).get();
//						Elements elementsInLargeImageDocument = largeImageDocument.getElementsByTag("a");
//						Element largeImageElement = null;
//						for(Element elementInLargeImageDocument : elementsInLargeImageDocument)
//						{
//							if(elementInLargeImageDocument.text().equals("查看原图"))
//							{
//								String largeImageHref = elementInLargeImageDocument.attr("href");
//								Element parentOfLink = link.parent();
//								link.remove();
//								Element smallImageLink = parentOfLink.getElementsByTag("img").first();
//								smallImageLink.attr("src", largeImageHref);
//								smallImageLink.text("");
//							}
//						}
//					}
//					catch(SocketTimeoutException e)
//					{								
//					}
//					finally
//					{								
//					}
//				}				
//			}				
//		}			
//		return contentElement.children();
//	}
}
