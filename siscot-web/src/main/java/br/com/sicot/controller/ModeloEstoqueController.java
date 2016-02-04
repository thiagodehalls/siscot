package br.com.sicot.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import br.com.sicot.data.EstoqueListProducer;
import br.com.sicot.data.ProdutoListProducer;
import br.com.sicot.model.Estoque;
import br.com.sicot.model.Produto;
import br.com.sicot.service.EstoqueRegistration;
import br.com.sicot.util.Constantes;
import br.com.sicot.util.EmpresaEstoque;

/**
 * Created by thiago on 06/01/16.
 */
@Named
@Stateful
@ConversationScoped
public class ModeloEstoqueController {

	@Inject
    private FacesContext facesContext;

    @Inject
    private Conversation conversation;
	
    @Inject
    private EstoqueListProducer estoqueListProducer;

    @Inject
    private EmpresaEstoque empresaEstoque;
    
    @Inject
    private EstoqueRegistration estoqueRegistration;
    
    @Inject
    private ProdutoListProducer produtoListProducer;

    private List<Estoque> estoques;
    private List<Produto> produtos;

    private Part file;

    public void export() throws IOException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        BufferedInputStream input = null;

        ServletOutputStream output = response.getOutputStream();

        response.reset();
        response.setContentType("application/x-download");

        response.setHeader("Content-Disposition", "attachment; filename=Estoque.xls");

        DecimalFormat format = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        format.setMinimumFractionDigits(2);
        format.setParseBigDecimal (true);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet();

        HSSFCellStyle lockedStyle = wb.createCellStyle();
        lockedStyle.setLocked(true);

        HSSFPatriarch drawing = sh.createDrawingPatriarch();
        HSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 7, 8);

        HSSFComment commentId = drawing.createCellComment(anchor);
        commentId.setString(new HSSFRichTextString("Este campo não deve ser editado, trata-se do identificador interno do SISCOT."));
        HSSFRow row = sh.createRow(0);
        HSSFCell cellId = row.createCell(0);
        cellId.setCellValue("Identificador");
        cellId.setCellComment(commentId);
        cellId.setCellStyle(lockedStyle);
        
        HSSFComment comment = drawing.createCellComment(anchor);
        comment.setString(new HSSFRichTextString("Aqui você deverá informa o código interno do produto que é usado em sua empresa para identifica-lo."));
        HSSFCell cell = row.createCell(1);
        cell.setCellValue("Código do Produto");
        cell.setCellComment(comment);
        cell.setCellStyle(lockedStyle);

        HSSFComment comment1 = drawing.createCellComment(anchor);
        comment1.setString(new HSSFRichTextString("Descrição do produto. Não há necessidade de alterar esta coluna"));
        HSSFCell cell1 = row.createCell(2);
        cell1.setCellValue("Produto");
        cell1.setCellComment(comment1);
        cell1.setCellStyle(lockedStyle);

        HSSFComment comment2 = drawing.createCellComment(anchor);
        comment2.setString(new HSSFRichTextString("Aqui você deverá informa o valor unitário do produto para estoque."));
        HSSFCell cell2 = row.createCell(3);
        cell2.setCellValue("Valor Unitário");
        cell2.setCellComment(comment2);
        cell2.setCellStyle(lockedStyle);

        HSSFComment comment3 = drawing.createCellComment(anchor);
        comment3.setString(new HSSFRichTextString("Aqui você deverá informa a quantidade do produto para estoque."));
        HSSFCell cell3 = row.createCell(4);
        cell3.setCellValue("Quantidade");
        cell3.setCellComment(comment3);
        cell3.setCellStyle(lockedStyle);

        HSSFComment comment4 = drawing.createCellComment(anchor);
        comment4.setString(new HSSFRichTextString("Informa a média do valor unitário do produto. Não há necessidade de alterar esta coluna"));
        HSSFCell cell4 = row.createCell(5);
        cell4.setCellValue("Valor Médio");
        cell4.setCellComment(comment4);
        cell4.setCellStyle(lockedStyle);

        for(int rownum = 0; rownum < estoques.size(); rownum++){
            HSSFRow _row = sh.createRow(rownum+1);

            HSSFCell _cellId = _row.createCell(0);
            _cellId.setCellValue(estoques.get(rownum).getProduto().getId());
            _cellId.setCellStyle(lockedStyle);
            
            HSSFCell _cell = _row.createCell(1);
            _cell.setCellValue(estoques.get(rownum).getCodigo());

            HSSFCell _cell1 = _row.createCell(2);
            _cell1.setCellValue(estoques.get(rownum).getProduto().getNome());
            _cell1.setCellStyle(lockedStyle);

            HSSFCell _cell2 = _row.createCell(3);
            _cell2.setCellValue(format.format(estoques.get(rownum).getValorUnitario()));

            HSSFCell _cell3 = _row.createCell(4);
            _cell3.setCellValue(estoques.get(rownum).getQuantidade().intValue());

            HSSFCell _cell4 = _row.createCell(5);
            _cell4.setCellValue(format.format(estoques.get(rownum).getProduto().getMedida()));
            _cell4.setCellStyle(lockedStyle);

        }

        sh.autoSizeColumn(0);
        sh.autoSizeColumn(1);
        sh.autoSizeColumn(2);
        sh.autoSizeColumn(3);
        sh.autoSizeColumn(4);
        sh.autoSizeColumn(5);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wb.write(baos);

        input = new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()), 1024);

        // Write file contents to response.
        byte[] buffer = baos.toByteArray();
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Finalize task.
        output.flush();
        output.close();
        input.close();

        response.flushBuffer();
        facesContext.responseComplete();
        facesContext.renderResponse();
    }

    @SuppressWarnings({ "rawtypes", "unused" })
    public void upload() throws ParseException{
    	DecimalFormat format = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols (new Locale ("pt", "BR")));  
		format.setMinimumFractionDigits(2);   
		format.setParseBigDecimal (true);
    	
        try{
            InputStream input = file.getInputStream();

            HSSFWorkbook wb = new HSSFWorkbook(input);
            HSSFSheet sheet = wb.getSheetAt(0);
            
			Iterator rowIter = sheet.rowIterator();
            
			int row = 0;
			
            while(rowIter.hasNext()){
               
            	if(row==0){
            		
            		rowIter.next();
            		row++;
            		
            		continue;
            	}
            	
            	HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                
                HSSFCell cell = (HSSFCell) cellIter.next();
                HSSFCell cell1 = (HSSFCell) cellIter.next();
                HSSFCell cell2 = (HSSFCell) cellIter.next();
                HSSFCell cell3 = (HSSFCell) cellIter.next();
                HSSFCell cell4 = (HSSFCell) cellIter.next();
                HSSFCell cell5 = (HSSFCell) cellIter.next();
                
                Estoque estoque = new Estoque();
                estoque.setProdutoId(new Long((int)cell.getNumericCellValue()));
                estoque.setCodigo(cell1.getStringCellValue().trim());
                estoque.setQuantidade(new BigDecimal(cell4.toString()));
                estoque.setValorUnitario(new BigDecimal(format.parse(cell3.toString()).doubleValue()));

                estoque.setDataFimPeriodo(new Date());
                estoque.setDataInicioPeriodo(new Date());
                estoque.setCnpj(this.empresaEstoque.getEmpresaEstoque().getCnpj());
                estoque.setEmpresa(this.empresaEstoque.getEmpresaEstoque());
                
                if(!estoques.contains(estoque)){
                	estoques.add(estoque);
                }
                else{
                	int index = estoques.indexOf(estoque);
                	
                	estoques.get(index).setCodigo(cell1.getStringCellValue().trim());
                	estoques.get(index).setQuantidade(new BigDecimal(cell4.toString()));
                	estoques.get(index).setValorUnitario(new BigDecimal(format.parse(cell3.toString()).doubleValue()));
                }
                
                row++;
            }

        }
        catch (IOException e) {
        	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Arquivo inválido!", "Arquivo inválido!");
        	facesContext.addMessage(null, msg);
        }
    }
    
    public void register() throws  Exception {
        this.conversation.end();
        try{

            for (Estoque estoque : estoques) {

                estoqueRegistration.register(estoque);
            }

            estoques.clear();

            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, Constantes.MSG_SUCESS, Constantes.MSG_SUCESS);
            facesContext.addMessage(null, m);

            init();
        }catch (Exception e){
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Falha ao Registrar.");
            facesContext.addMessage(null, m);
        }
    }
    
    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

    @PostConstruct
    private void init() {
    	
        if (this.conversation.isTransient()) {
            this.conversation.begin();
            this.conversation.setTimeout(1800000L);
        }
        
        if(this.empresaEstoque.getEmpresaEstoque()!=null){
        	String id = this.empresaEstoque.getEmpresaEstoque().getCnpj();
            estoques = estoqueListProducer.findEstoqueOrderByProduto(id);
            produtoListProducer.findProdutoOrderByNome();
            produtos = produtoListProducer.getProdutos();
        }

        if(estoques!=null) {
            for (Estoque estoque : estoques) {
            	produtos.remove(estoque.getProduto());
            }
            
            for (Produto produto : produtos) {
            	Estoque estoque = new Estoque();

                estoque.setProduto(produto);
                estoque.setProdutoId(produto.getId());
                estoque.setDataInicioPeriodo(new Date());
                estoque.setDataFimPeriodo(new Date());
                estoque.setCnpj(this.empresaEstoque.getEmpresaEstoque().getCnpj());
                estoque.setQuantidade(new BigDecimal(0));
                estoque.setValorUnitario(new BigDecimal(0));
                estoque.setEmpresa(this.empresaEstoque.getEmpresaEstoque());
                estoque.setCodigo("");
                
                estoques.add(estoque);
            }
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

	public List<Estoque> getEstoques() {
		return estoques;
	}

	public void setEstoques(List<Estoque> estoques) {
		this.estoques = estoques;
	}
    
}
