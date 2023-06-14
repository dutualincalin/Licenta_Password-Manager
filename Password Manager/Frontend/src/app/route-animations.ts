import {
  trigger,
  transition,
  style,
  query,
  group,
  animate, state
} from "@angular/animations";

export const fadeInOut = trigger('fadeInOut', [
  state('shown', style({opacity: 1})),
  state('hidden', style({opacity: 0})),
  transition('hidden => shown', [animate('500ms ease-in')]),
  transition('shown => hidden', [animate('500ms ease-out')]),
]);

export const slider =
  trigger('routeAnimations',[
    transition('* => isUp', slideTo('top')),
    transition('* => isRight', slideTo('right')),
    transition('isUp => *', slideTo('bottom')),
    transition('isDown => *', slideTo('top')),
    transition('isLeft => *', slideTo('right')),
    transition('isRight => *', slideTo('left'))
  ]);

function slideTo(direction: string) {
  const optional = {optional: true};

  if(direction == 'bottom') {
    return [
      query(':enter, :leave', [
        style({
          position: 'absolute',
          left: 0,
          top: '0',
          width: '100%'
        })
      ], optional),

      query(':enter', [
        style({top: '100%'})
      ]),

      group([
        query(':leave', [
          animate('1s ease', style({top: '-100%'}))
        ], optional),
        query(':enter', [
          animate('1s ease', style({top: '0%'}))
        ], optional)
      ])
    ]
  }

  let animatingQuery = [
    query(':enter', [
      style({[direction]: '-100%'})
    ]),
    group([
      query(':leave', [
        animate('1s ease', style({[direction]: '100%'}))
      ], optional),
      query(':enter', [
        animate('1s ease', style({[direction]: '0%'}))
      ], optional)
    ])
  ];

  switch (direction) {
    case 'left':
    case 'right':
      animatingQuery.unshift(
        query(':enter, :leave', [
          style({
            position: 'absolute',
            top: 0,
            [direction]: 0,
            width: '100%'
          })
        ], optional)
      );
      break;

    case 'top':
      animatingQuery.unshift(
        query(':enter, :leave', [
          style({
            position: 'absolute',
            left: 0,
            [direction]: 0,
            width: '100%'
          })
        ], optional)
      );
      break;
  }


  return animatingQuery;
}
