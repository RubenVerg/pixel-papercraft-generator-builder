module Thumbnail = {
  @react.component
  let make = (~thumbnail: Generator.thumnbnailDef) => {
    <div className="mb-8 border bg-gray-100 p-4 w-72 h-72">
      <img src={thumbnail.url} width="256" height="256" />
    </div>
  }
}

module Video = {
  // https://web.dev/aspect-ratio/
  @react.component
  let make = (~video: Generator.videoDef) => {
    <div className="mb-8" style={ReactDOM.Style.make(~maxWidth="640px", ())}>
      <div
        style={ReactDOM.Style.make(
          ~position="relative",
          ~width="100%",
          ~maxWidth="640px",
          ~paddingTop="56.25%",
          (),
        )}>
        <iframe
          style={ReactDOM.Style.make(
            ~position="absolute",
            ~top="0",
            ~width="100%",
            ~height="100%",
            (),
          )}
          src={video.url}
          allowFullScreen={true}
        />
      </div>
    </div>
  }
}

module Instructions = {
  @react.component
  let make = (~instructions: Generator.instructionsDef) => {
    <div className="mb-8"> {instructions} </div>
  }
}

module GeneratorInfo = {
  @react.component
  let make = (~generatorDef: Generator.generatorDef) => {
    <div>
      {switch generatorDef.video {
      | Some(video) => <Video video={video} />
      | None =>
        switch generatorDef.thumbnail {
        | Some(thumbnail) => <Thumbnail thumbnail={thumbnail} />
        | None => React.null
        }
      }}
      {switch generatorDef.instructions {
      | Some(instructions) => <Instructions instructions={instructions} />
      | None => React.null
      }}
    </div>
  }
}

@react.component
let make = (~generatorDef: Builder.generatorDef) => {
  let (model, setModel) = React.useState(_ => None)

  React.useEffect1(() => {
    ResourceLoader.loadResources(generatorDef)
    ->Promise.thenResolve(results => {
      switch results {
      | Error(exn) => Js.log(exn)
      | Ok(imageTuples, textureTuples) => {
          let model = Builder.Model.make()
          let model =
            imageTuples->Js.Array2.reduce(
              (acc, (id, image)) => Builder.addImage(acc, id, image),
              model,
            )
          let model =
            textureTuples->Js.Array2.reduce(
              (acc, (id, texture)) => Builder.addTexture(acc, id, texture),
              model,
            )
          ScriptRunner.run(generatorDef, model)
          ->Promise.thenResolve(model => {
            setModel(_ => Some(model))
          })
          ->ignore
        }
      }
    })
    ->ignore

    None
  }, [generatorDef])

  let runScript = model => {
    ScriptRunner.run(generatorDef, model)
    ->Promise.thenResolve(model => {
      setModel(_ => Some(model))
    })
    ->ignore
  }

  let onInputsChange = model => {
    runScript(model)
  }

  let onPagesInputsChange = () => {
    runScript(Generator.getModel())
  }

  <div>
    {switch model {
    | None => React.null
    | Some(model) =>
      <div>
        <GeneratorInfo generatorDef={generatorDef} />
        <GeneratorInputs model={model} onChange={onInputsChange} />
        <GeneratorPages generatorDef={generatorDef} model={model} onChange={onPagesInputsChange} />
      </div>
    }}
  </div>
}

let default = make
