import React, {Component, PropTypes} from 'react'
import {Crud} from '../crud'
import {id, ids, handleAssociationChange} from '../crud/Associations'
import {AtividadesController} from '../controllers'
import {Input, Row, Col, Grid, Panel, Glyphicon, Button} from 'react-bootstrap';

class Atividades extends Component {
    componentDidMount = () => AtividadesController.list() // Busca inicial

    searchSchema = (search) =>
        <Input type="text" placeholder="Buscar por nome do Plano de Aula" autoComplete="off"
               name="nome" degaultValue={search.nome}/>

    listSchema = {
        header: () =>
            <tr>
                <th>ID</th>
                <th>Conteúdo</th>
                <th>Data</th>
            </tr>,

        body: (atividade) =>
            <tr>
                <td>{atividade.id}</td>
                <td>{atividade.nome}</td>
                <td>{atividade.data}</td>
            </tr>,
            
       actions: (atividade) =>  
       	 <div>
       		<Button id={atividade.id} bsStyle="link" disabled={atividade.executada} onClick={() => window.open("planejamentoAtividades?id="+atividade.id
       				)} ><Glyphicon glyph="inbox" title="Planejamento"/></Button>
       		<Button id={atividade.id} bsStyle="link" onClick={() => window.open("impressaoAtividades?id="+atividade.id
       	       				)} ><Glyphicon glyph="print" title="Imprimir" /></Button>
       	    <Button id={atividade.id} bsStyle="link" onClick={() => window.open("execucaoAtividades?id="+atividade.id
   	       				)} ><Glyphicon glyph="ok" title="Lista de chamada" /></Button>       	       				
       	 </div>     
            
    }

    formSchema = (atividade, {tipos = [], planejamentos = []}) =>
        <div>
	        <Grid fluid>
		        <Row className="show-grid">	
		        	<Col xs={12}>
			        	<Input type="select" label="Modalidade" name="tipoAtividade"
				                defaultValue={id(atividade.tipoAtividade)} onChange={handleAssociationChange}>
				            <option value="">Selecione...</option>
				            {tipos.map((element, i) =>
				                <option key={i} value={element.id}>{element.nome}</option>
				            )}
			            </Input>
		            </Col>
		        </Row>
				<Row className="show-grid">	
		        	<Col xs={12} md={4}><Input type="text" label="Data" placeholder="Data" name="data" defaultValue={atividade.data}/></Col>
		        	<Col xs={12} md={8}>
			        	<Input type="text" label="Conteúdo" placeholder="Conteúdo" name="nome" defaultValue={atividade.nome}/>
		            </Col>
	            </Row>
		        <Row className="show-grid">
		        	<Col xs={12}><Input type="textarea" label="Comentário" name="comentario" defaultValue={atividade.comentario} placeholder="Comentário"  /></Col>
		        </Row>
	      </Grid>
        </div>

    render = () =>
        <Crud title="Plano de Aula"
              controller={AtividadesController}
              searchSchema={this.searchSchema}
              listSchema={this.listSchema}
              formSchema={this.formSchema} />
}

export default Atividades